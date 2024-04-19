import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEstudios } from 'app/shared/model/estudios.model';
import { getEntities as getEstudios } from 'app/entities/estudios/estudios.reducer';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import { getEntity, updateEntity, createEntity, reset } from './veterinario.reducer';

export const VeterinarioUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estudios = useAppSelector(state => state.estudios.entities);
  const veterinarioEntity = useAppSelector(state => state.veterinario.entity);
  const loading = useAppSelector(state => state.veterinario.loading);
  const updating = useAppSelector(state => state.veterinario.updating);
  const updateSuccess = useAppSelector(state => state.veterinario.updateSuccess);

  const handleClose = () => {
    navigate('/veterinario' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEstudios({}));
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
      ...veterinarioEntity,
      ...values,
      estudios: mapIdList(values.estudios),
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
          ...veterinarioEntity,
          estudios: veterinarioEntity?.estudios?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.veterinario.home.createOrEditLabel" data-cy="VeterinarioCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.veterinario.home.createOrEditLabel">Create or edit a Veterinario</Translate>
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
                  id="veterinario-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.veterinario.nombre')}
                id="veterinario-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.veterinario.apellido')}
                id="veterinario-apellido"
                name="apellido"
                data-cy="apellido"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.veterinario.direccion')}
                id="veterinario-direccion"
                name="direccion"
                data-cy="direccion"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.veterinario.telefono')}
                id="veterinario-telefono"
                name="telefono"
                data-cy="telefono"
                type="text"
                validate={{
                  maxLength: { value: 9, message: translate('entity.validation.maxlength', { max: 9 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.veterinario.especilidad')}
                id="veterinario-especilidad"
                name="especilidad"
                data-cy="especilidad"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.veterinario.estudios')}
                id="veterinario-estudios"
                data-cy="estudios"
                type="select"
                multiple
                name="estudios"
              >
                <option value="" key="0" />
                {estudios
                  ? estudios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/veterinario" replace color="info">
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

export default VeterinarioUpdate;
