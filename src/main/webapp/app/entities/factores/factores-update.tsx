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
import { IFactores } from 'app/shared/model/factores.model';
import { getEntity, updateEntity, createEntity, reset } from './factores.reducer';

export const FactoresUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const enfermedads = useAppSelector(state => state.enfermedad.entities);
  const factoresEntity = useAppSelector(state => state.factores.entity);
  const loading = useAppSelector(state => state.factores.loading);
  const updating = useAppSelector(state => state.factores.updating);
  const updateSuccess = useAppSelector(state => state.factores.updateSuccess);

  const handleClose = () => {
    navigate('/factores' + location.search);
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
      ...factoresEntity,
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
          ...factoresEntity,
          enfermedads: factoresEntity?.enfermedads?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.factores.home.createOrEditLabel" data-cy="FactoresCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.factores.home.createOrEditLabel">Create or edit a Factores</Translate>
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
                  id="factores-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.factores.nombre')}
                id="factores-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.factores.tipo')}
                id="factores-tipo"
                name="tipo"
                data-cy="tipo"
                type="text"
                validate={{
                  maxLength: { value: 40, message: translate('entity.validation.maxlength', { max: 40 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.factores.descripcion')}
                id="factores-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.factores.enfermedad')}
                id="factores-enfermedad"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/factores" replace color="info">
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

export default FactoresUpdate;
