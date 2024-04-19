import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMedicamento } from 'app/shared/model/medicamento.model';
import { getEntities as getMedicamentos } from 'app/entities/medicamento/medicamento.reducer';
import { IEnfermedad } from 'app/shared/model/enfermedad.model';
import { getEntities as getEnfermedads } from 'app/entities/enfermedad/enfermedad.reducer';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import { getEntities as getVeterinarios } from 'app/entities/veterinario/veterinario.reducer';
import { IMascota } from 'app/shared/model/mascota.model';
import { getEntities as getMascotas } from 'app/entities/mascota/mascota.reducer';
import { IHistorial } from 'app/shared/model/historial.model';
import { getEntity, updateEntity, createEntity, reset } from './historial.reducer';

export const HistorialUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const medicamentos = useAppSelector(state => state.medicamento.entities);
  const enfermedads = useAppSelector(state => state.enfermedad.entities);
  const veterinarios = useAppSelector(state => state.veterinario.entities);
  const mascotas = useAppSelector(state => state.mascota.entities);
  const historialEntity = useAppSelector(state => state.historial.entity);
  const loading = useAppSelector(state => state.historial.loading);
  const updating = useAppSelector(state => state.historial.updating);
  const updateSuccess = useAppSelector(state => state.historial.updateSuccess);

  const handleClose = () => {
    navigate('/historial' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMedicamentos({}));
    dispatch(getEnfermedads({}));
    dispatch(getVeterinarios({}));
    dispatch(getMascotas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...historialEntity,
      ...values,
      medicamentos: mapIdList(values.medicamentos),
      enfermedads: mapIdList(values.enfermedads),
      veterinario: veterinarios.find(it => it.id.toString() === values.veterinario?.toString()),
      mascota: mascotas.find(it => it.id.toString() === values.mascota?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...historialEntity,
          medicamentos: historialEntity?.medicamentos?.map(e => e.id.toString()),
          enfermedads: historialEntity?.enfermedads?.map(e => e.id.toString()),
          veterinario: historialEntity?.veterinario?.id,
          mascota: historialEntity?.mascota?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.historial.home.createOrEditLabel" data-cy="HistorialCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.historial.home.createOrEditLabel">Create or edit a Historial</Translate>
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
                  id="historial-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.historial.fechaConsulta')}
                id="historial-fechaConsulta"
                name="fechaConsulta"
                data-cy="fechaConsulta"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.historial.diagnostico')}
                id="historial-diagnostico"
                name="diagnostico"
                data-cy="diagnostico"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.historial.medicamento')}
                id="historial-medicamento"
                data-cy="medicamento"
                type="select"
                multiple
                name="medicamentos"
              >
                <option value="" key="0" />
                {medicamentos
                  ? medicamentos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('veterinarySystemApp.historial.enfermedad')}
                id="historial-enfermedad"
                data-cy="enfermedad"
                type="select"
                multiple
                name="enfermedads"
              >
                <option value="" key="0" />
                {enfermedads
                  ? enfermedads.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="historial-veterinario"
                name="veterinario"
                data-cy="veterinario"
                label={translate('veterinarySystemApp.historial.veterinario')}
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
                id="historial-mascota"
                name="mascota"
                data-cy="mascota"
                label={translate('veterinarySystemApp.historial.mascota')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/historial" replace color="info">
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

export default HistorialUpdate;
