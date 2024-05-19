import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity as createNewCita} from 'app/entities/cita/cita.reducer';
import { createEntity as createNewDueno, getEntities as getDuenos, getEntity as getDueno, updateEntity} from 'app/entities/dueno/dueno.reducer';
import { createEntity, getEntities as getMascotas, updateEntity as updateMascota} from 'app/entities/mascota/mascota.reducer';
import { ICita } from 'app/shared/model/cita.model';
import { IDueno } from 'app/shared/model/dueno.model';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import React, { useEffect, useState } from 'react';
import { Modal, Button } from 'react-bootstrap'; 
import { ValidatedField, ValidatedForm, isEmail, isNumber, translate } from 'react-jhipster';
import AddMascotaForm from './AddMascotaForm';
import axios from 'axios';
import { IMascota } from 'app/shared/model/mascota.model';
import './sliderCita.css';
import dayjs from 'dayjs';
import { createEntity as createNewUsuario, getEntities} from 'app/entities/usuario/usuario.reducer';
import { IUsuario } from 'app/shared/model/usuario.model';
import { IUser } from 'app/shared/model/user.model';
import { handleRegister, reset } from 'app/modules/account/register/register.reducer';
import { toast } from 'react-toastify';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { PDFDownloadLink } from '@react-pdf/renderer';
import CitaPDF from './PDFCita';


const ModalAddCita = ({ isOpen, toggle, veterinario,citas}
    :{
        isOpen:boolean;
        toggle:()=>void;
        veterinario:IVeterinario;
        citas:ICita[]|null
    }) => {
    const [parteFormulario, setParteFormulario] = useState(1);
    const [cita,setCita] = useState<ICita>({});
    const [selectedDueno,setSeletedDueno] = useState<IDueno>({});

    const dispatch = useAppDispatch()
    const mascotaList = useAppSelector(state => state.mascota.entities);
    const duenoList = useAppSelector(state => state.dueno.entities);
    const [timeValue, setTimeValue] = useState('08:00');
    const currentLocale = useAppSelector(state => state.locale.currentLocale);
    const successMessage = useAppSelector(state => state.register.successMessage);

    //control de hora:
    const handleTimeChange = (event) => {
        // Extraer la hora seleccionada del evento
        const newTime = event.target.value;
        // Actualizar solo las horas y mantener los minutos como están
        const updatedTime = newTime.slice(0, 3) + timeValue.slice(3);
        // Actualizar el estado con el nuevo valor de tiempo
        setTimeValue(updatedTime);
    };

    useEffect(()=>{
        dispatch(getMascotas({page:0,size:999,sort:`id,asc`}));
        dispatch(getDuenos({page:0,size:999,sort:`id,asc`}));
        dispatch(reset());
    },[])
    useEffect(() => {
        if (successMessage) {
          toast.success(translate(successMessage));
        }
      }, [successMessage]);
    
    const goToFormDueno = () => setParteFormulario(4);
    const goToFormMascota = () => {
        setParteFormulario(5)
    };
    const goToEndForm = ()=> setParteFormulario(3)


const validateHora = (hora) => {
    // Obtener la hora actual usando Day.js
    const horaActual = dayjs().hour();
    const minutosActuales = dayjs().minute();

    // Dividir la hora proporcionada en partes (horas y minutos)
    const [horas, minutos] = hora.split(':');

    // Convertir horas y minutos a números enteros
    const horaProporcionada = parseInt(horas, 10);
    const minutosProporcionados = parseInt(minutos, 10);

    // Verificar si las horas son mayores a la hora actual
    if (horaProporcionada > horaActual) {
        return true;
    } else if (horaProporcionada === horaActual) {
        // Si la hora es igual a la hora actual, también verificamos los minutos
        return minutosProporcionados > minutosActuales;
    } else {
        // Si la hora es menor a la hora actual, la hora no es válida
        return false;
    }
};

const handleNext = (values) => {
    // Validaciones
    if (!values.hora || !values.motivo || !values.fecha) {
        toast.error("Todos los campos son obligatorios.");
        return;
    }

    // Validar si la fecha es hoy
    const isToday = dayjs(values.fecha).isSame(dayjs(), 'day');

    if (isToday && !validateHora(values.hora)) {
        toast.error("La hora debe ser posterior a la hora actual.");
        return;
    }

    if (citas) {
        const isSameDate = citas.some(cita => dayjs(cita.fecha).isSame(values.fecha, 'day'));
    
        if (isSameDate) {
            let citaMismaHoraYfecha = false;
    
            citas.forEach(cita => {
                const horaCita = cita.hora ? dayjs(cita.hora, 'HH:mm') : null;
                const isHoraValida = horaCita && horaCita.isValid();
                const isSameTime = isHoraValida && horaCita.format('HH:mm') === values.hora;
                if (isSameTime) {
                    citaMismaHoraYfecha = true;
                }
            });
    
            if (citaMismaHoraYfecha) {
                toast.error('Hay una cita con la misma fecha y hora.');
                return;
            }
        }
    }
    

    const current = {
        'hora': values.hora,
        'fecha': values.fecha,
        'motivo': values.motivo,
        'veterinario': veterinario
    };

    setCita(current);
    setParteFormulario(2);
};


    const handleNext2of3 = (values) => {



        if(!values.nombreDeDueno) return 
        if(values.nombreDeDueno===0)return
       
        const regex = new RegExp(`${values.nombreDeDueno.replace(/\s+/g, '\\s+')}`, 'i');

    
        const dueñoEncontrado = duenoList.find(d => (d.nombre + ' ' + d.apellido).match(regex));

        if (dueñoEncontrado) {
            setSeletedDueno(dueñoEncontrado);
            setParteFormulario(3); 
        } else {
            toast.error("Ese dueño NO existe")
        }
    }

    const handleClose = () => {
        toggle(); 
        window.location.reload();
    }

    const saveEntity = async (values) => {
        let mascotasSeleccionadas = await mascotaList.filter(mascota => values.mascotas.includes(mascota?.id?.toString()));
        
        console.log("Mascotas seleccionadas", mascotasSeleccionadas);
        let newMascota = null;
        if (mascotasSeleccionadas.length === 0) {
            const today = new Date();
            const formattedDate = today.toLocaleDateString().split('/').join('-'); // Formato: MM-DD-YYYY
            newMascota = await ((await dispatch(createEntity({
                "nIdentificacionCarnet": -1,
                "foto": `${formattedDate}_captured_image.png`,
                "fechaNacimiento": dayjs(),
                'dueno': selectedDueno,
                'especie':{id:4},
                'raza':{id:62}
            }))).payload as any).data;
        }
    
        if (newMascota) mascotasSeleccionadas.push(newMascota);
    
        if (mascotasSeleccionadas.length > 2) {
            toast.error("NO se puede elegir más de dos animales por cita");
            return;
        }
        
        const entity = {
            ...cita,
            'mascotas': mascotasSeleccionadas
        }
        console.log("nueva", entity);
        const nueva = await ((await dispatch(createNewCita(entity))).payload as any).data;
    
        const nuevasMascotas = await Promise.all(mascotasSeleccionadas.map(async m => {
            if (!m) return null;
            const nuevaMascota = { ...m, citas: [...m.citas, nueva] };
            console.log("nueva mascota", nuevaMascota);
            const updatedMascota = await dispatch(updateMascota(nuevaMascota));
            console.log("Mascota actualizada:", updatedMascota);
            return nuevaMascota;
        }));
    
        console.log("nueva", nueva);
        //handleClose();
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
        if(!values.email) return 
        if(values.email.length===0)return


        const newUser = await (await dispatch(handleRegister({
            login: values.nombre,
            email: values.email,
            password: '1234',
            langKey: currentLocale,
          })));
        
          const allusuarios = ((await dispatch(getEntities({ page: 0, size: 999, sort: 'id,asc' }))).payload as any).data;
      
          const promises = allusuarios.map(async (u: IUsuario) => {
              if (u.dueno && u.dueno.id) {
                  const response = await dispatch(getDueno(u.dueno.id));
                  return (response.payload as any).data;
              }
              return null;
          });
      
          const duenoActual = await Promise.all(promises);
          console.log(duenoActual);
          
          let duenoFiltrado = duenoActual.filter((dueno: IDueno) => {
            return dueno !== null && dueno.nombre.toLowerCase() === values.nombre.toLowerCase();
        });
        
        console.log(duenoFiltrado);
        
         
          const newduenoFiltrado:IDueno = {
            'id':duenoFiltrado[0].id,
            'nombre':duenoFiltrado[0].nombre,
            'telefono':values.telefono,
            'apellido':values.apellido,
            'direccion':values.direccion,
          }

          dispatch(updateEntity(newduenoFiltrado));
          
        
        setSeletedDueno(newduenoFiltrado);
        setParteFormulario(3);
    }
    console.log(mascotaList);
    console.log(duenoList);
    console.log(cita);

    
    
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
                                value={timeValue}
                                onChange={handleTimeChange}
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
                            {(selectedDueno&&mascotaList)? (
                                mascotaList.map((m,i) => {
                                    console.log(m.dueno.id);
                                    
                                    if (m.dueno.id === selectedDueno.id) {
                                        return (
                                            <>
                
                                            <option value={m.id} key={m.id} >
                                                Nº de carnet:{m.nIdentificacionCarnet}
                                            </option>
                                            </>
                                        );
                                    }
                                })
                            ) : (
                                <p>Debe agregar mascotas para poder tener una cita.</p>
                            )}
                            </ValidatedField>
                            <PDFDownloadLink
                            document={
                                <CitaPDF
                                    fechaCita={cita.fecha && cita.fecha}
                                    motivo={cita.motivo}
                                    nombreSolicitante={selectedDueno.nombre}
                                />
                            }
                            fileName="cita.pdf"
                                >
                                {({ blob, url, loading, error }) =>
                                    loading ? 'Generando documento...' : 'Inprimir cita'
                                }
                            </PDFDownloadLink>
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
                         name="email"
                        label={translate('global.form.email.label')}
                        placeholder={translate('global.form.email.placeholder')}
                         type="email"
                        validate={{
                            required: { value: true, message: translate('global.messages.validate.email.required') },
                            minLength: { value: 5, message: translate('global.messages.validate.email.minlength') },
                            maxLength: { value: 254, message: translate('global.messages.validate.email.maxlength') },
                            validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
                        }}
                    data-cy="email"
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
