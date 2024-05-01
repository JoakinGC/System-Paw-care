import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEstetica } from 'app/shared/model/estetica.model';
import { getEntities as getEsteticas } from 'app/entities/estetica/estetica.reducer';
import { ICuidadoraHotel } from 'app/shared/model/cuidadora-hotel.model';
import { getEntities as getCuidadoraHotels } from 'app/entities/cuidadora-hotel/cuidadora-hotel.reducer';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import { getEntities as getVeterinarios } from 'app/entities/veterinario/veterinario.reducer';
import { IMascota } from 'app/shared/model/mascota.model';
import { getEntities, getEntities as getMascotas } from 'app/entities/mascota/mascota.reducer';
import { ICita } from 'app/shared/model/cita.model';
import { getEntity, updateEntity, createEntity, reset } from './cita.reducer';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';

export const CitaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const esteticas = useAppSelector(state => state.estetica.entities);
  const cuidadoraHotels = useAppSelector(state => state.cuidadoraHotel.entities);
  const veterinarios = useAppSelector(state => state.veterinario.entities);
  const mascotas = useAppSelector(state => state.mascota.entities);
  const citaEntity = useAppSelector(state => state.cita.entity);
  const loading = useAppSelector(state => state.cita.loading);
  const updating = useAppSelector(state => state.cita.updating);
  const updateSuccess = useAppSelector(state => state.cita.updateSuccess);

  const handleClose = () => {
    navigate('/cita' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEsteticas({}));
    dispatch(getCuidadoraHotels({}));
    dispatch(getVeterinarios({}));
    dispatch(getMascotas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = async values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }


    const allMascotas = await dispatch(getEntities({}));
    

    const mascotas =(allMascotas.payload as any).data.filter(mascota => values.mascotas.includes(mascota.id.toString())) ;


    console.log(mascotas);
    
    const entity = {
      ...citaEntity,
      ...values,
      estetica: esteticas.find(it => it.id.toString() === values.estetica?.toString()),
      cuidadoraHotel: cuidadoraHotels.find(it => it.id.toString() === values.cuidadoraHotel?.toString()),
      veterinario: veterinarios.find(it => it.id.toString() === values.veterinario?.toString()),
      mascotas
    };
    console.log(entity);
    
    if (isNew) {
      const newEn = await dispatch(createEntity(entity));
      console.log(newEn);
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...citaEntity,
          estetica: citaEntity?.estetica?.id,
          cuidadoraHotel: citaEntity?.cuidadoraHotel?.id,
          veterinario: citaEntity?.veterinario?.id,
          mascotas: citaEntity?.mascotas?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.cita.home.createOrEditLabel" data-cy="CitaCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.cita.home.createOrEditLabel">Create or edit a Cita</Translate>
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
                  id="cita-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('veterinarySystemApp.cita.hora')} id="cita-hora" name="hora" data-cy="hora" type="time" />
              <ValidatedField
                label={translate('veterinarySystemApp.cita.fecha')}
                id="cita-fecha"
                name="fecha"
                data-cy="fecha"
                type="date"
              />
              <ValidatedField
                label={translate('veterinarySystemApp.cita.motivo')}
                id="cita-motivo"
                name="motivo"
                data-cy="motivo"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                id="cita-estetica"
                name="estetica"
                data-cy="estetica"
                label={translate('veterinarySystemApp.cita.estetica')}
                type="select"
              >
                <option value="" key="0" />
                {esteticas
                  ? esteticas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cita-cuidadoraHotel"
                name="cuidadoraHotel"
                data-cy="cuidadoraHotel"
                label={translate('veterinarySystemApp.cita.cuidadoraHotel')}
                type="select"
              >
                <option value="" key="0" />
                {cuidadoraHotels
                  ? cuidadoraHotels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cita-veterinario"
                name="veterinario"
                data-cy="veterinario"
                label={translate('veterinarySystemApp.cita.veterinario')}
                type="select"
              >
                <option value="" key="0" />
                {veterinarios
                  ? veterinarios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('veterinarySystemApp.cita.mascota')}
                id="cita-mascota"
                data-cy="mascota"
                type="select"
                multiple
                name="mascotas"
              >
                <option value="" key="0" />
                {mascotas
                  ? mascotas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cita" replace color="info">
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

export default CitaUpdate;
