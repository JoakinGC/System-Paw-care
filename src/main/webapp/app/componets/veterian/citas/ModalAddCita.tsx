import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity as createNewCita} from 'app/entities/cita/cita.reducer';
import { createEntity, getEntities as getDuenos} from 'app/entities/dueno/dueno.reducer';
import { getEntities as getMascotas, updateEntity} from 'app/entities/mascota/mascota.reducer';
import { ICita } from 'app/shared/model/cita.model';
import { IDueno } from 'app/shared/model/dueno.model';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import React, { useEffect, useState } from 'react';
import { Modal, Button } from 'react-bootstrap'; 
import { ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import AddMascotaForm from './AddMascotaForm';
import axios from 'axios';
import { IMascota } from 'app/shared/model/mascota.model';


const ModalAddCita = ({ isOpen, toggle, veterinario }
    :{
        isOpen:boolean;
        toggle:()=>void;
        veterinario:IVeterinario;
    }) => {
    const [parteFormulario, setParteFormulario] = useState(1);
    const [cita,setCita] = useState<ICita>({});
    const [selectedDueno,setSeletedDueno] = useState<IDueno>({});

    const dispatch = useAppDispatch()
    const mascotaList = useAppSelector(state => state.mascota.entities);
    const loading = useAppSelector(state => state.mascota.loading);
    const duenoList = useAppSelector(state => state.dueno.entities);
    const loadingDueno = useAppSelector(state => state.dueno.loading);
    const [imageUrl, setImageUrl] = useState<string | null>(null);

    useEffect(()=>{
        dispatch(getMascotas({page:0,size:999,sort:`id,asc`}));
        dispatch(getDuenos({page:0,size:999,sort:`id,asc`}));
    },[])
        const fetchImage = async (urlImg) => {
          try {
            const imageUrl = await obtenerImagen(urlImg);
            setImageUrl(imageUrl);
          } catch (error) {
            console.error('Error al obtener la imagen:', error);
          }
        };
        
      

    const goToFormDueno = () => setParteFormulario(4);
    const goToFormMascota = () => {
        setParteFormulario(5)
    };
    const goToEndForm = ()=> setParteFormulario(3)
    const validateHora = (hora) => {
        // Obtener la hora actual
        const horaActual = new Date().getHours();
        const minutosActuales = new Date().getMinutes();
    
        // Dividir la hora proporcionada en partes (horas y minutos)
        const [horas, minutos] = hora.split(':');
        
        // Convertir horas y minutos a números enteros
        const horaProporcionada = parseInt(horas, 10);
        const minutosProporcionados = parseInt(minutos, 10);
    
        // Verificar si la hora proporcionada es posterior a la hora actual
        if (horaProporcionada < horaActual || (horaProporcionada === horaActual && minutosProporcionados <= minutosActuales)) {
            return false;
        }
        
        // Verificar si los minutos son cero (00)
        return minutosProporcionados === 0;
    };

    const handleNext = (values) => {

        //validaciones

        if(values.hora.length===0) return
        if(values.motivo.length===0) return
        if(values.fecha.length===0) return
        if (!validateHora(values.hora)) {
            alert("La hora debe ser posterior a la hora actual y los minutos deben ser 00");
            return;
        }
        if(values.motivo.length===0)return


        const current = {
            'hora':values.hora,
            'fecha':values.fecha,
            'motivo':values.motivo,
            veterinario
        }

        setCita(current)
        setParteFormulario(2);
    }

    const handleNext2of3 = (values) => {


        if(!values.nombreDeDueno) return 
        if(values.nombreDeDueno===0)return
       
        const regex = new RegExp(`${values.nombreDeDueno.replace(/\s+/g, '\\s+')}`, 'i');

    
        const dueñoEncontrado = duenoList.find(d => (d.nombre + ' ' + d.apellido).match(regex));

        if (dueñoEncontrado) {
            setSeletedDueno(dueñoEncontrado);
            setParteFormulario(3);  
        } else {
            alert("Ese dueño NO existe")
        }
    }

    const handleClose = () => {
        toggle(); 
        window.location.reload();
    }

    const saveEntity = async (values) => {
        
        
        const mascotasSeleccionadas:IMascota[] = await mascotaList.filter(mascota => values.mascotas.includes(mascota.id.toString()));
        
        console.log("Mascotas sleccioada",mascotasSeleccionadas);

        if(mascotasSeleccionadas.length>2) return
        
        const entity = {
            ...cita,
            'mascotas':mascotasSeleccionadas
        }
        console.log("nueva",entity);
        const nueva:ICita = await((await dispatch(createNewCita(entity))).payload as any).data;


        const nuevasMascotas = await mascotasSeleccionadas.map(m => {
            const nuevaMascota = { ...m };
            nuevaMascota.citas = [...nuevaMascota.citas, nueva];
            console.log("nueva mascota",nuevaMascota);
            
            return nuevaMascota;
        });
        
        nuevasMascotas.map(async m => {
            const ac = await dispatch(updateEntity(m));
            console.log("Mascota actulziada:" ,ac);
            
        });
        console.log("nueva",nueva);
        handleClose();
    }

    const formAddNewDueno = async(values) =>{
        if(!values.apellido) return 
        if(values.apellido.length===0)return
        if(!values.direccion) return 
        if(values.direccion.length===0)return
        if(!values.nombre) return 
        if(values.nombre.length===0)return
        if(!values.telefono) return 
        if(values.telefono.length===0)return

        const newDueno = {
            'apellido':values.apellido,
            'direccion':values.direccion,
            'nombre':values.nombre,
            'telefono':values.telefono,
        }

        const duenoEntity = ((await dispatch(createEntity(newDueno))).payload as any).data;
        setSeletedDueno(duenoEntity);
        setParteFormulario(3);
    }
    console.log(mascotaList);
    console.log(duenoList);
    console.log(cita);

    const obtenerImagen = async (fileName) => {
        try {
          const response = await axios.get(`http://localhost:9000/api/images/${fileName}`, {
            responseType: 'arraybuffer'
          });
  
          if (response.status !== 200) {
            throw new Error('Error al obtener la imagen');
          }
  
          const blob = new Blob([response.data], { type: response.headers['content-type'] });
          const imageUrl = URL.createObjectURL(blob);
  
          return imageUrl;
        } catch (error) {
          console.error('Error:', error);
          return null;
        }
      };
    return (
        <>
            <Modal show={isOpen} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Agrega cita</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {parteFormulario === 1 && (
                        <ValidatedForm defaultValues={{}} onSubmit={handleNext}>
                            <ValidatedField
                                label={translate('veterinarySystemApp.cita.hora')}
                                id="cita-hora"
                                name="hora"
                                data-cy="hora"
                                type="time"
                                step="3600" // Define el incremento en segundos (3600 segundos = 1 hora)
                                min="08:00" 
                                max="17:00"
                                required={true}
                            />
                            <ValidatedField
                                label={translate('veterinarySystemApp.cita.fecha')}
                                id="cita-fecha"
                                name="fecha"
                                data-cy="fecha"
                                type="date"
                                required={true}
                            />
                            <ValidatedField
                                label={translate('veterinarySystemApp.cita.motivo')}
                                id="cita-motivo"
                                name="motivo"
                                data-cy="motivo"
                                type="text"
                                required={true}
                                validate={{
                                    maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                                }}
                            />
                            <Button variant="primary" type="submit">
                                Siguiente
                            </Button>
                        </ValidatedForm>
                    )}
                    {parteFormulario === 2 && (
                        <>
                            <ValidatedForm onSubmit={handleNext2of3}>
                            {/* Campo para ingresar el nombre del dueño */}
                            <ValidatedField
                                label={translate('veterinarySystemApp.cita.nombreDeDueno')}
                                id="nombreDeDueno"
                                name="nombreDeDueno"
                                data-cy="nombre"
                                type="text"
                                required={true}
                                validate={{
                                    maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                                }}
                            />
                            <Button variant="primary" type="submit">
                                Siguiente
                            </Button>
                            <Button style={{marginLeft:4}} variant="primary" onClick={goToFormDueno}>
                                Agregar nuevo propietario
                            </Button>
                        </ValidatedForm>
                            
                            
                        </>
                    )}
                     {parteFormulario === 3 && (
                        <ValidatedForm onSubmit={saveEntity}>
                            <p>Selecciona unas de tus mascotas {selectedDueno&&`${selectedDueno.nombre} ${selectedDueno.apellido}`}:</p>
                            <ValidatedField
                                label={translate('veterinarySystemApp.cita.mascota')}
                                id="cita-mascota"
                                data-cy="mascota"
                                type="select"
                                multiple
                                name="mascotas"
                                required={true}
                            >
                            <option value="" key="0" />    
                            {selectedDueno ? (
                                mascotaList.map((m) => {
                                    if (m.dueno.id === selectedDueno.id) {
                                        return (
                                            <option value={m.id} key={m.id} >
                                                {imageUrl && <img src={imageUrl} alt="Imagen de la mascota"/>}
                                                {m.nIdentificacionCarnet}
                                            </option>
                                        );
                                    }
                                })
                            ) : (
                                <p>Debe agregar mascotas para poder tener una cita.</p>
                            )}
                            </ValidatedField>
                            <Button type="submit" variant="primary">
                                Guardar Cita
                            </Button>
                            <Button onClick={goToFormMascota} style={{marginLeft:3}} variant="primary">
                                Agregar mascota
                            </Button>
                        </ValidatedForm>
                    )}

                    {parteFormulario === 4 && (
                        <>
                    <ValidatedForm onSubmit={formAddNewDueno}>   
                    <ValidatedField
                        label={translate('veterinarySystemApp.dueno.nombre')}
                        id="dueno-nombre"
                        name="nombre"
                        data-cy="nombre"
                        type="text"
                        required={true}
                        validate={{
                         maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                        }}
                        />
                    <ValidatedField
                        label={translate('veterinarySystemApp.dueno.apellido')}
                        id="dueno-apellido"
                        name="apellido"
                        data-cy="apellido"
                        type="text"
                        required={true}
                        validate={{
                                maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                            }}
                        />
                        <ValidatedField
                            label={translate('veterinarySystemApp.dueno.direccion')}
                            id="dueno-direccion"
                            name="direccion"
                            data-cy="direccion"
                            type="text"
                            required={true}
                            validate={{
                                maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                            }}
                        />
                        <ValidatedField
                            label={translate('veterinarySystemApp.dueno.telefono')}
                            id="dueno-telefono"
                            name="telefono"
                            data-cy="telefono"
                            type="text"
                            required={true}
                            validate={{
                                    maxLength: { value: 9, message: translate('entity.validation.maxlength', { max: 9 }) },
                                }}
                            />
                            <Button variant="primary" type="submit">
                                Siguiente
                            </Button>
                        </ValidatedForm>
                            
                        </>
                    )}
                    {parteFormulario === 5 && (
                      <AddMascotaForm toggle={goToEndForm} dueno={selectedDueno} key={0}/>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Cerrar
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}

export default ModalAddCita;
