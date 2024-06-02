import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICita } from 'app/shared/model/cita.model';
import { getEntities as getCitas } from 'app/entities/cita/cita.reducer';
import { IDueno } from 'app/shared/model/dueno.model';
import { getEntities as getDuenos } from 'app/entities/dueno/dueno.reducer';
import { IEspecie } from 'app/shared/model/especie.model';
import { getEntities as getEspecies } from 'app/entities/especie/especie.reducer';
import { IRaza } from 'app/shared/model/raza.model';
import { getEntities as getRazas } from 'app/entities/raza/raza.reducer';
import { IMascota } from 'app/shared/model/mascota.model';
import { getMascota, updateEntity, createEntity, reset } from './mascota.reducer';
import axios from 'axios';

export const MascotaUpdate = () => {
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

  

  const handleClose = () => {
   navigate('/' + location.search);
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
    dispatch(getRazas({page:0,size:999,sort:`id,asc`}));

    

  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = async values => {
        
    if (values.nIdentificacionCarnet !== undefined && typeof values.nIdentificacionCarnet !== 'number' &&values.nIdentificacionCarnet) {
      values.nIdentificacionCarnet = Number(values.nIdentificacionCarnet);
    }
    
    const foto = mascotaEntity.foto;
    console.log(mascotaEntity.foto);
    

    const entity = {
      ...mascotaEntity,
      ...values,
      citas: mapIdList(values.citas),
      dueno: duenos.find(it => it.id.toString() === values.dueno?.toString()),
      especie: especies.find(it => it.id.toString() === values.especie?.toString()),
      raza: razas.find(it => it.id.toString() === values.raza?.toString()),
    };
    if (!selectedFile) {
      alert('Por favor selecciona un archivo.');
      return;
    }
    
    const id = await  dispatch(updateEntity(entity));
    const fotoBlob = new Blob([values.foto], { type: values.foto.type });
    const formData = new FormData();
    formData.append('file', selectedFile, foto);

    try {
      const response = await axios.post('http://localhost:9000/api/images/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      const imageUrl = response.data;
      console.log('URL de la imagen:', imageUrl);
      console.log('Respuesta del servidor:', response.data);
      alert('Imagen subida con éxito.');
    } catch (error) {
      console.error('Error al subir la imagen:', error);
      alert('Error al subir la imagen. Por favor intenta de nuevo.');
    }
    window.location.reload()
  };

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };
  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...mascotaEntity,
          citas: mascotaEntity?.citas?.map(e => e.id.toString()),
          dueno: mascotaEntity?.dueno?.id,
          especie: mascotaEntity?.especie?.id,
          raza: mascotaEntity?.raza?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.mascota.home.createOrEditLabel" data-cy="MascotaCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.mascota.home.createOrEditLabel">Create or edit a Mascota</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="mascota-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.mascota.nIdentificacionCarnet')}
                id="mascota-nIdentificacionCarnet"
                name="nIdentificacionCarnet"
                data-cy="nIdentificacionCarnet"
                type="text"
                required={true}
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
                required={true}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              
              <ValidatedField
                id="mascota-dueno"
                name="dueno"
                data-cy="dueno"
                required={true}
                label={translate('veterinarySystemApp.mascota.dueno')}
                type="select"
              >
                <option value="" key="0" />
                {duenos
                  ? duenos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="mascota-especie"
                name="especie"
                data-cy="especie"
                required={true}
                label={translate('veterinarySystemApp.mascota.especie')}
                type="select"
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
                required={true}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/mascotasVeterian" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MascotaUpdate;



