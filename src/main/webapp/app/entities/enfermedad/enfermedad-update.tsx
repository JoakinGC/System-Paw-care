import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRaza } from 'app/shared/model/raza.model';
import { getEntities as getRazas } from 'app/entities/raza/raza.reducer';
import { IEspecie } from 'app/shared/model/especie.model';
import { getEntities as getEspecies } from 'app/entities/especie/especie.reducer';
import { ITerapia } from 'app/shared/model/terapia.model';
import { getEntities as getTerapias } from 'app/entities/terapia/terapia.reducer';
import { IFactores } from 'app/shared/model/factores.model';
import { getEntities as getFactores } from 'app/entities/factores/factores.reducer';
import { IHistorial } from 'app/shared/model/historial.model';
import { getEntities as getHistorials } from 'app/entities/historial/historial.reducer';
import { IEnfermedad } from 'app/shared/model/enfermedad.model';
import { getEntity, updateEntity, createEntity, reset } from './enfermedad.reducer';

export const EnfermedadUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const razas = useAppSelector(state => state.raza.entities);
  const especies = useAppSelector(state => state.especie.entities);
  const terapias = useAppSelector(state => state.terapia.entities);
  const factores = useAppSelector(state => state.factores.entities);
  const historials = useAppSelector(state => state.historial.entities);
  const enfermedadEntity = useAppSelector(state => state.enfermedad.entity);
  const loading = useAppSelector(state => state.enfermedad.loading);
  const updating = useAppSelector(state => state.enfermedad.updating);
  const updateSuccess = useAppSelector(state => state.enfermedad.updateSuccess);

  const handleClose = () => {
    navigate('/enfermedad' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRazas({}));
    dispatch(getEspecies({}));
    dispatch(getTerapias({}));
    dispatch(getFactores({}));
    dispatch(getHistorials({}));
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
      ...enfermedadEntity,
      ...values,
      razas: mapIdList(values.razas),
      especies: mapIdList(values.especies),
      terapias: mapIdList(values.terapias),
      factores: mapIdList(values.factores),
      historials: mapIdList(values.historials),
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
          ...enfermedadEntity,
          razas: enfermedadEntity?.razas?.map(e => e.id.toString()),
          especies: enfermedadEntity?.especies?.map(e => e.id.toString()),
          terapias: enfermedadEntity?.terapias?.map(e => e.id.toString()),
          factores: enfermedadEntity?.factores?.map(e => e.id.toString()),
          historials: enfermedadEntity?.historials?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.enfermedad.home.createOrEditLabel" data-cy="EnfermedadCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.enfermedad.home.createOrEditLabel">Create or edit a Enfermedad</Translate>
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
                  id="enfermedad-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.nombre')}
                id="enfermedad-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.descripcion')}
                id="enfermedad-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.raza')}
                id="enfermedad-raza"
                data-cy="raza"
                type="select"
                multiple
                name="razas"
              >
                <option value="" key="0" />
                {razas
                  ? razas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.especie')}
                id="enfermedad-especie"
                data-cy="especie"
                type="select"
                multiple
                name="especies"
              >
                <option value="" key="0" />
                {especies
                  ? especies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.terapia')}
                id="enfermedad-terapia"
                data-cy="terapia"
                type="select"
                multiple
                name="terapias"
              >
                <option value="" key="0" />
                {terapias
                  ? terapias.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.factores')}
                id="enfermedad-factores"
                data-cy="factores"
                type="select"
                multiple
                name="factores"
              >
                <option value="" key="0" />
                {factores
                  ? factores.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.historial')}
                id="enfermedad-historial"
                data-cy="historial"
                type="select"
                multiple
                name="historials"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/enfermedad" replace color="info">
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

export default EnfermedadUpdate;
