import React, { useEffect, useState } from 'react';
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
import { getEntity as getHistorial, updateEntity as updateHistorial, createEntity as createHistorial, reset  as resetHistorial} from '../../entities/historial/historial.reducer';
import { getEntity as getTratamiento, updateEntity as updateTratamiento, createEntity as createTratamiento, reset as resetTratamientos } from '../../entities/tratamiento/tratamiento.reducer';
import { getEntities as getHistorials } from 'app/entities/historial/historial.reducer';

const FormVeterian = () =>{
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const medicamentos = useAppSelector(state => state.medicamento.entities);
  const enfermedads = useAppSelector(state => state.enfermedad.entities);
  const veterinarios = useAppSelector(state => state.veterinario.entities);
  const mascotas = useAppSelector(state => state.mascota.entities);
  const historialEntity = useAppSelector(state => state.historial.entity);
  const loadingHistorial = useAppSelector(state => state.historial.loading);
  const updatingHistorial = useAppSelector(state => state.historial.updating);
  const updateSuccessHistorial = useAppSelector(state => state.historial.updateSuccess);
  const historials = useAppSelector(state => state.historial.entities);
  const tratamientoEntity = useAppSelector(state => state.tratamiento.entity);
  const loadingTratamiento = useAppSelector(state => state.tratamiento.loading);
  const updatingTratamiento = useAppSelector(state => state.tratamiento.updating);
  const updateSuccessTratamiento = useAppSelector(state => state.tratamiento.updateSuccess);


  const handleClose = () => {
    navigate('/historial' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(resetHistorial());
      dispatch(resetTratamientos());
    } else {
      dispatch(getHistorial(id));
      dispatch(getTratamiento(id));
    }

    dispatch(getMedicamentos({}));
    dispatch(getEnfermedads({}));
    dispatch(getVeterinarios({}));
    dispatch(getMascotas({}));
    dispatch(getHistorials({}));
  }, []);

  useEffect(() => {
    if (updateSuccessHistorial) {
      handleClose();
    }
    if (updateSuccessTratamiento) {
      handleClose();
    }
  }, [updateSuccessHistorial,updateSuccessTratamiento]);

  // eslint-disable-next-line complexity
  const saveEntity = async values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }


    const entityHistorial = {
      ...historialEntity,
      ...values,
      medicamentos: mapIdList(values.medicamentos),
      enfermedads: mapIdList(values.enfermedads),
      veterinario: veterinarios.find(it => it.id.toString() === values.veterinario?.toString()),
      mascota: mascotas.find(it => it.id.toString() === values.mascota?.toString()),
    };

    if (isNew) {
      const newHistorial = await dispatch(createHistorial(entityHistorial));
      const entityTratamiento = {
        ...tratamientoEntity,
        ...values,
        newHistorial
      };
      dispatch(createTratamiento(entityTratamiento));
    } else {
      const newHistorial = await dispatch(updateHistorial(entityHistorial));
      const entityTratamiento = {
        ...tratamientoEntity,
        ...values,
        newHistorial
      };
      dispatch(updateTratamiento(entityTratamiento));
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
          {loadingHistorial ? (
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
                        {otherEntity.nombre}
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
                        {otherEntity.nombre}
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
                        {otherEntity.nombre}
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
                        {otherEntity.nIdentificacionCarnet }
                      </option>
                    ))
                  : null}
              </ValidatedField>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="tratamiento-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.tratamiento.fechaInicio')}
                id="tratamiento-fechaInicio"
                name="fechaInicio"
                data-cy="fechaInicio"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.tratamiento.fechaFin')}
                id="tratamiento-fechaFin"
                name="fechaFin"
                data-cy="fechaFin"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.tratamiento.notas')}
                id="tratamiento-notas"
                name="notas"
                data-cy="notas"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />

              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/historial" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updatingHistorial}>
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
}

export default FormVeterian;
