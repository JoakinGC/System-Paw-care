import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { Modal, ModalHeader, ModalBody, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { ValidatedField, ValidatedForm, isEmail, isNumber, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCitas } from 'app/entities/cita/cita.reducer';
import {  getEntities as getDuenos, getEntity as getDueno, updateEntity } from 'app/entities/dueno/dueno.reducer';
import { getEntities as getEspecies } from 'app/entities/especie/especie.reducer';
import { getEntities as getRazas } from 'app/entities/raza/raza.reducer';
import { IDueno } from 'app/shared/model/dueno.model';
import { createEntity as createMascota, getMascota, getEntities as getMascotas, reset, updateEntity as updateMascota} from '../../../entities/mascota/mascota.reducer';
import { useNavigate, useParams } from 'react-router';
import { mapIdList } from 'app/shared/util/entity-utils';
import { IMascota } from 'app/shared/model/mascota.model';
import { toast } from 'react-toastify';
import { handleRegister } from 'app/modules/account/register/register.reducer';
import { getEntities  as getUsuarios} from 'app/entities/usuario/usuario.reducer';
import { IUsuario } from 'app/shared/model/usuario.model';

const AddMascotaModal = ({ isOpen, toggle }: { isOpen: any; toggle: any;}) => {
    const dispatch = useAppDispatch();

    const navigate = useNavigate();
  
    const { id } = useParams<'id'>();
    const isNew = id === undefined;
  
    const especies = useAppSelector(state => state.especie.entities);
    const razas = useAppSelector(state => state.raza.entities);
    const mascotaEntity = useAppSelector(state => state.mascota.entity);
    const loading = useAppSelector(state => state.mascota.loading);
    const updateSuccess = useAppSelector(state => state.mascota.updateSuccess);
    const duenoList = useAppSelector(state => state.dueno.entities);
    const [newMascota,setNewMascota] = useState<IMascota>({})
    const currentLocale = useAppSelector(state => state.locale.currentLocale);

    const [selectedFile, setSelectedFile] = useState(null);
    const [selectedDueno,setSeletedDueno] = useState<IDueno>({});
    
    const [part, setPart] = useState(1);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
      };
    const part1 = (values)=>{ 

      console.log(values);
      if(!values.fechaNacimiento) return 
      if(values.fechaNacimiento.length===0)return
      if(!values.fechaNacimiento) return 
      if(values.nIdentificacionCarnet.length===0)return

      const currentNew = {
        'fechaNacimiento':values.fechaNacimiento,
        'nIdentificacionCarnet':values.nIdentificacionCarnet
      }
      setNewMascota(currentNew)
      setPart(2);
    }
    
      const handleNext = () => {
        setPart(part + 1);
      };
    
      const handlePrev = () => {
        setPart(part - 1);
      };


      const handleNext2of3 = (values) => {

        console.log(values);
        
        if(!values.nombreDeDueno) return 
        if(values.nombreDeDueno.length===0)return
       
        const regex = new RegExp(`${values.nombreDeDueno.replace(/\s+/g, '\\s+')}`, 'i');

    
        const dueñoEncontrado = duenoList.find(d => (d.nombre + ' ' + d.apellido).match(regex));

        if (dueñoEncontrado) {
            setSeletedDueno(dueñoEncontrado);
            handleNext(); 
        } else {
            toast.error("Ese dueño NO existe")
        }
    }
   
    useEffect(() => {
        if (isNew) {
          dispatch(reset());
        } else {
          dispatch(getMascota(id));
        }
    
        dispatch(getCitas({page:0,size:999,sort:`id,asc`}));
        dispatch(getDuenos({page:0,size:999,sort:`id,asc`}));
        dispatch(getEspecies({page:0,size:999,sort:`id,asc`}));
        dispatch(getRazas({page:0,size:999,sort:`id,asc`}));
    
        
    
      }, []);

      useEffect(() => {
        if (updateSuccess) {
          
          toggle();
        }
      }, [updateSuccess]);

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
        
          const allusuarios = ((await dispatch(getUsuarios({ page: 0, size: 999, sort: 'id,asc' }))).payload as any).data;
      
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
        setPart(3);
    }

      const saveEntity = async values => {
        
        if (values.nIdentificacionCarnet !== undefined && typeof values.nIdentificacionCarnet !== 'number' &&values.nIdentificacionCarnet) {
          values.nIdentificacionCarnet = Number(values.nIdentificacionCarnet);
        }
        

        if (!selectedFile) {
            toast.error('Por favor selecciona un archivo.');
            return;
        }
        
        const today = new Date();
        const formattedDate = today.toLocaleDateString().split('/').join('-'); // Formato: MM-DD-YYYY
        const foto = `${formattedDate}_captured_image.png`;
        const dueno = selectedDueno;
    
        const entity = {
          ...newMascota,
          ...values,
          dueno,
          foto,
          especie: especies.find(it => it.id.toString() === values.especie?.toString()),
          raza: razas.find(it => it.id.toString() === values.raza?.toString()),
        };
        
        
        const id = await  dispatch(createMascota(entity));
        
        const formData = new FormData();
        formData.append('file', selectedFile, foto);

        try {
          const response = await axios.post('http://localhost:9000/api/images/upload', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });
          const imageUrl = response.data.url;
          console.log('URL de la imagen:', imageUrl);
          console.log('Respuesta del servidor:', response.data);
          toast.success('Imagen subida con éxito.');
        } catch (error) {
          console.error('Error al subir la imagen:', error);
          toast.error('Error al subir la imagen. Por favor intenta de nuevo.');
        }
        //window.location.reload()
      };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...mascotaEntity,
          especie: mascotaEntity?.especie?.id,
          raza: mascotaEntity?.raza?.id,
        };

    const goToFormDueno = () => setPart(4)

    return (
      <Modal isOpen={isOpen} toggle={toggle}>
        <ModalHeader toggle={toggle}>Agregar Mascota</ModalHeader>
        <ModalBody>
          {loading ? (
            <p>Loading...</p>
          ) : (
            <>
              {part === 1 && (
                <>
                  <ValidatedForm defaultValues={defaultValues()} onSubmit={part1}>
                    <ValidatedField
                      label={translate('veterinarySystemApp.mascota.nIdentificacionCarnet')}
                      id="mascota-nIdentificacionCarnet"
                      name="nIdentificacionCarnet"
                      data-cy="nIdentificacionCarnet"
                      type="text"
                      validate={{
                        required: { value: true, message: translate('entity.validation.required') },
                        validate: v => isNumber(v) || translate('entity.validation.number'),
                      }}
                    />
                    <input type="file" accept="image/*" onChange={handleFileChange} />
    
                    <ValidatedField
                      label={translate('veterinarySystemApp.mascota.fechaNacimiento')}
                      id="mascota-fechaNacimiento"
                      name="fechaNacimiento"
                      data-cy="fechaNacimiento"
                      type="date"
                      validate={{
                        required: { value: true, message: translate('entity.validation.required') },
                      }}
                    />
                    <Button color='primary' type='submit'>Siguiente</Button>
                  </ValidatedForm>
                </>
              )}
    
              {part === 3 && (
                <>
                  <ValidatedForm onSubmit={saveEntity}>
                    <ValidatedField
                      id="mascota-especie"
                      name="especie"
                      data-cy="especie"
                      label={translate('veterinarySystemApp.mascota.especie')}
                      type="select"
                      validate={{
                        required: { value: true, message: "Requiere que eligas una especie" },
                      }}
                    >
                      <option value="" key="0" />
                      {especies
                        ? especies.map(otherEntity => (
                            <option value={otherEntity.id} key={otherEntity.id}>
                              {otherEntity.nombre}
                            </option>
                          ))
                        : null}
                    </ValidatedField>
                    <ValidatedField
                      id="mascota-raza"
                      name="raza"
                      data-cy="raza"
                      label={translate('veterinarySystemApp.mascota.raza')}
                      type="select"
                      validate={{
                        required: { value: true, message: "Requiere que eligas una raza" },
                      }}
                    >
                      <option value="" key="0" />
                      {razas
                        ? razas.map(otherEntity => (
                            <option value={otherEntity.id} key={otherEntity.id}>
                              {otherEntity.nombre}
                            </option>
                          ))
                        : null}
                    </ValidatedField>
                    <Button color="primary" type="submit">
                      Guardar
                    </Button>
                    <Button color="secondary" onClick={toggle}>
                      Cancelar
                    </Button>
                  </ValidatedForm>
                </>
              )}
    
              {part === 2 && (
                <>
                  <ValidatedForm onSubmit={handleNext2of3}>
                    {/* Campo para ingresar el nombre del dueño */}
                    <ValidatedField
                      label={'Ingresa el nombre del dueño de la mascota'}
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
    
              {part === 4 && (
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
            </>
          )}
        </ModalBody>
      </Modal>
    );
};

export default AddMascotaModal;
