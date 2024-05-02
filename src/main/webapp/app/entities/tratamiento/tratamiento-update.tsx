import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IHistorial } from 'app/shared/model/historial.model';
import { getEntities as getHistorials } from 'app/entities/historial/historial.reducer';
import { ITratamiento } from 'app/shared/model/tratamiento.model';
import { getEntity as getTratamiento, updateEntity as updateTratamiento, createEntity as createTratamiento, reset as resetTratamientos } from './tratamiento.reducer';

export const TratamientoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const historials = useAppSelector(state => state.historial.entities);
  const tratamientoEntity = useAppSelector(state => state.tratamiento.entity);
  const loadingTratamiento = useAppSelector(state => state.tratamiento.loading);
  const updatingTratamiento = useAppSelector(state => state.tratamiento.updating);
  const updateSuccessTratamiento = useAppSelector(state => state.tratamiento.updateSuccess);

  const handleClose = () => {
    navigate('/tratamiento' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(resetTratamientos());
    } else {
      dispatch(getTratamiento(id));
    }

    dispatch(getHistorials({}));
  }, []);

  useEffect(() => {
    if (updateSuccessTratamiento) {
      handleClose();
    }
  }, [updateSuccessTratamiento]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entityTratamiento = {
      ...tratamientoEntity,
      ...values,
      historial: historials.find(it => it.id.toString() === values.historial?.toString()),
    };

    if (isNew) {
      dispatch(createTratamiento(entityTratamiento));
    } else {
      dispatch(updateTratamiento(entityTratamiento));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...tratamientoEntity,
          historial: tratamientoEntity?.historial?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.tratamiento.home.createOrEditLabel" data-cy="TratamientoCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.tratamiento.home.createOrEditLabel">Create or edit a Tratamiento</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loadingTratamiento ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
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
              <ValidatedField
                id="tratamiento-historial"
                name="historial"
                data-cy="historial"
                label={translate('veterinarySystemApp.tratamiento.historial')}
                type="select"
              >
                <option value="" key="0" />
                {historials
                  ? historials.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tratamiento" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updatingTratamiento}>
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

export default TratamientoUpdate;
