import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICuidadoraHotel } from 'app/shared/model/cuidadora-hotel.model';
import { getEntity, updateEntity, createEntity, reset } from './cuidadora-hotel.reducer';

export const CuidadoraHotelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cuidadoraHotelEntity = useAppSelector(state => state.cuidadoraHotel.entity);
  const loading = useAppSelector(state => state.cuidadoraHotel.loading);
  const updating = useAppSelector(state => state.cuidadoraHotel.updating);
  const updateSuccess = useAppSelector(state => state.cuidadoraHotel.updateSuccess);

  const handleClose = () => {
    navigate('/cuidadora-hotel' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...cuidadoraHotelEntity,
      ...values,
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
          ...cuidadoraHotelEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.cuidadoraHotel.home.createOrEditLabel" data-cy="CuidadoraHotelCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.cuidadoraHotel.home.createOrEditLabel">Create or edit a CuidadoraHotel</Translate>
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
                  id="cuidadora-hotel-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.cuidadoraHotel.nombre')}
                id="cuidadora-hotel-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.cuidadoraHotel.direccion')}
                id="cuidadora-hotel-direccion"
                name="direccion"
                data-cy="direccion"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.cuidadoraHotel.telefono')}
                id="cuidadora-hotel-telefono"
                name="telefono"
                data-cy="telefono"
                type="text"
                validate={{
                  maxLength: { value: 9, message: translate('entity.validation.maxlength', { max: 9 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.cuidadoraHotel.servicioOfrecido')}
                id="cuidadora-hotel-servicioOfrecido"
                name="servicioOfrecido"
                data-cy="servicioOfrecido"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cuidadora-hotel" replace color="info">
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

export default CuidadoraHotelUpdate;
