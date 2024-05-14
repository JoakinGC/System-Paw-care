import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { Modal, ModalHeader, ModalBody, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCitas } from 'app/entities/cita/cita.reducer';
import { getEntities as getDuenos } from 'app/entities/dueno/dueno.reducer';
import { getEntities as getEspecies } from 'app/entities/especie/especie.reducer';
import { getEntities as getRazas } from 'app/entities/raza/raza.reducer';
import { IDueno } from 'app/shared/model/dueno.model';
import { createEntity, getMascota, getEntities as getMascotas, reset, updateEntity} from '../../../entities/mascota/mascota.reducer';
import { useNavigate, useParams } from 'react-router';
import { mapIdList } from 'app/shared/util/entity-utils';

const AddMascotaForm = ({  toggle,dueno }: {  toggle:any,dueno: IDueno }) => {
    const dispatch = useAppDispatch();

    const navigate = useNavigate();
  
    const { id } = useParams<'id'>();
    const isNew = id === undefined;
  
    const citas = useAppSelector(state => state.cita.entities);
    const duenos = useAppSelector(state => state.dueno.entities);
    const especies = useAppSelector(state => state.especie.entities);
    const razas = useAppSelector(state => state.raza.entities);
    const mascotaEntity = useAppSelector(state => state.mascota.entity);
    const loading = useAppSelector(state => state.mascota.loading);
    const updating = useAppSelector(state => state.mascota.updating);
    const updateSuccess = useAppSelector(state => state.mascota.updateSuccess);

    const [selectedFile, setSelectedFile] = useState(null);
    const [capturedImage, setCapturedImage] = useState(null);
    


    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
      };
    
    const displayCapturedImage = (blob) => {
        setCapturedImage(URL.createObjectURL(blob));
    };

    const handleCameraCapture = async () => {
        try {
          const mediaStream = await navigator.mediaDevices.getUserMedia({ video: true });
          console.log(mediaStream);
          
          const mediaStreamTrack = mediaStream.getVideoTracks()[0];
          console.log(mediaStreamTrack);
          const imageCapture = new window.ImageCapture(mediaStreamTrack); 
          console.log(imageCapture);
          
    
          const blob = await imageCapture.takePhoto();
          console.log(blob);
          
          setSelectedFile(blob);
          displayCapturedImage(blob);
        } catch (error) {
          console.error('Error al capturar la imagen:', error);
        }
      };

    useEffect(() => {
        if (isNew) {
          dispatch(reset());
        } else {
          dispatch(getMascota(id));
        }
    
        dispatch(getCitas({}));
        dispatch(getDuenos({}));
        dispatch(getEspecies({}));
        dispatch(getRazas({}));
    
        
    
      }, []);

      useEffect(() => {
        if (updateSuccess) {
            
          toggle();
        }
      }, [updateSuccess]);

      const saveEntity = async values => {
        
        if (values.nIdentificacionCarnet !== undefined && typeof values.nIdentificacionCarnet !== 'number' &&values.nIdentificacionCarnet) {
          values.nIdentificacionCarnet = Number(values.nIdentificacionCarnet);
        }
        

        if (!selectedFile) {
            alert('Por favor selecciona un archivo.');
            return;
        }
        
        const today = new Date();
        const formattedDate = today.toLocaleDateString().split('/').join('-'); // Formato: MM-DD-YYYY
        const foto = `${formattedDate}_captured_image.png`;

    
        const entity = {
          ...mascotaEntity,
          ...values,
          foto,
          dueno,
          especie: especies.find(it => it.id.toString() === values.especie?.toString()),
          raza: razas.find(it => it.id.toString() === values.raza?.toString()),
        };
        
        
        const id = await  dispatch(createEntity(entity));
        
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
          alert('Imagen subida con éxito.');
        } catch (error) {
          console.error('Error al subir la imagen:', error);
          alert('Error al subir la imagen. Por favor intenta de nuevo.');
        }
      };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...mascotaEntity,
          dueno,
          especie: mascotaEntity?.especie?.id,
          raza: mascotaEntity?.raza?.id,
        };

  return (
    <>
      
      
      {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
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
              <button type="button" onClick={handleCameraCapture}>Tomar Foto</button>

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
          </ValidatedForm>
            )}
      
    </>
  );
};

export default AddMascotaForm;
