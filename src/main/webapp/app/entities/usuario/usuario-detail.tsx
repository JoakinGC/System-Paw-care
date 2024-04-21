import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './usuario.reducer';

export const UsuarioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();
  console.log(id)
  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const usuarioEntity = useAppSelector(state => state.usuario.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="usuarioDetailsHeading">
          <Translate contentKey="veterinarySystemApp.usuario.detail.title">Usuario</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{usuarioEntity.id}</dd>
          <dt>
            <span id="nombreUsuario">
              <Translate contentKey="veterinarySystemApp.usuario.nombreUsuario">Nombre Usuario</Translate>
            </span>
          </dt>
          <dd>{usuarioEntity.nombreUsuario}</dd>
          <dt>
            <span id="rol">
              <Translate contentKey="veterinarySystemApp.usuario.rol">Rol</Translate>
            </span>
          </dt>
          <dd>{usuarioEntity.rol}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.usuario.estetica">Estetica</Translate>
          </dt>
          <dd>{usuarioEntity.estetica ? usuarioEntity.estetica.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.usuario.veterinario">Veterinario</Translate>
          </dt>
          <dd>{usuarioEntity.veterinario ? usuarioEntity.veterinario.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.usuario.dueno">Dueno</Translate>
          </dt>
          <dd>{usuarioEntity.dueno ? usuarioEntity.dueno.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/usuario" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/usuario/${usuarioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UsuarioDetail;
