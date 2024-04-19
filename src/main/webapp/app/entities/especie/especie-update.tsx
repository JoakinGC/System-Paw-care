import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEnfermedad } from 'app/shared/model/enfermedad.model';
import { getEntities as getEnfermedads } from 'app/entities/enfermedad/enfermedad.reducer';
import { IEspecie } from 'app/shared/model/especie.model';
import { getEntity, updateEntity, createEntity, reset } from './especie.reducer';

export const EspecieUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const enfermedads = useAppSelector(state => state.enfermedad.entities);
  const especieEntity = useAppSelector(state => state.especie.entity);
  const loading = useAppSelector(state => state.especie.loading);
  const updating = useAppSelector(state => state.especie.updating);
  const updateSuccess = useAppSelector(state => state.especie.updateSuccess);

  const handleClose = () => {
    navigate('/especie' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEnfermedads({}));
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
      ...especieEntity,
      ...values,
      enfermedads: mapIdList(values.enfermedads),
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
          ...especieEntity,
          enfermedads: especieEntity?.enfermedads?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.especie.home.createOrEditLabel" data-cy="EspecieCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.especie.home.createOrEditLabel">Create or edit a Especie</Translate>
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
                  id="especie-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.especie.nombre')}
                id="especie-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.especie.nombreCientifico')}
                id="especie-nombreCientifico"
                name="nombreCientifico"
                data-cy="nombreCientifico"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.especie.enfermedad')}
                id="especie-enfermedad"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/especie" replace color="info">
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

export default EspecieUpdate;
