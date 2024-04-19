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
import { IVeterinario } from 'app/shared/model/veterinario.model';
import { getEntities as getVeterinarios } from 'app/entities/veterinario/veterinario.reducer';
import { IDueno } from 'app/shared/model/dueno.model';
import { getEntities as getDuenos } from 'app/entities/dueno/dueno.reducer';
import { IUsuario } from 'app/shared/model/usuario.model';
import { getEntity, updateEntity, createEntity, reset } from './usuario.reducer';

export const UsuarioUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const esteticas = useAppSelector(state => state.estetica.entities);
  const veterinarios = useAppSelector(state => state.veterinario.entities);
  const duenos = useAppSelector(state => state.dueno.entities);
  const usuarioEntity = useAppSelector(state => state.usuario.entity);
  const loading = useAppSelector(state => state.usuario.loading);
  const updating = useAppSelector(state => state.usuario.updating);
  const updateSuccess = useAppSelector(state => state.usuario.updateSuccess);

  const handleClose = () => {
    navigate('/usuario' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEsteticas({}));
    dispatch(getVeterinarios({}));
    dispatch(getDuenos({}));
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
      ...usuarioEntity,
      ...values,
      estetica: esteticas.find(it => it.id.toString() === values.estetica?.toString()),
      veterinario: veterinarios.find(it => it.id.toString() === values.veterinario?.toString()),
      dueno: duenos.find(it => it.id.toString() === values.dueno?.toString()),
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
          ...usuarioEntity,
          estetica: usuarioEntity?.estetica?.id,
          veterinario: usuarioEntity?.veterinario?.id,
          dueno: usuarioEntity?.dueno?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.usuario.home.createOrEditLabel" data-cy="UsuarioCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.usuario.home.createOrEditLabel">Create or edit a Usuario</Translate>
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
                  id="usuario-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.usuario.nombreUsuario')}
                id="usuario-nombreUsuario"
                name="nombreUsuario"
                data-cy="nombreUsuario"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.usuario.rol')}
                id="usuario-rol"
                name="rol"
                data-cy="rol"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                id="usuario-estetica"
                name="estetica"
                data-cy="estetica"
                label={translate('veterinarySystemApp.usuario.estetica')}
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
                id="usuario-veterinario"
                name="veterinario"
                data-cy="veterinario"
                label={translate('veterinarySystemApp.usuario.veterinario')}
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
                id="usuario-dueno"
                name="dueno"
                data-cy="dueno"
                label={translate('veterinarySystemApp.usuario.dueno')}
                type="select"
              >
                <option value="" key="0" />
                {duenos
                  ? duenos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/usuario" replace color="info">
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

export default UsuarioUpdate;
