import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './veterinario.reducer';

export const VeterinarioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const veterinarioEntity = useAppSelector(state => state.veterinario.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="veterinarioDetailsHeading">
          <Translate contentKey="veterinarySystemApp.veterinario.detail.title">Veterinario</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{veterinarioEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.veterinario.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{veterinarioEntity.nombre}</dd>
          <dt>
            <span id="apellido">
              <Translate contentKey="veterinarySystemApp.veterinario.apellido">Apellido</Translate>
            </span>
          </dt>
          <dd>{veterinarioEntity.apellido}</dd>
          <dt>
            <span id="direccion">
              <Translate contentKey="veterinarySystemApp.veterinario.direccion">Direccion</Translate>
            </span>
          </dt>
          <dd>{veterinarioEntity.direccion}</dd>
          <dt>
            <span id="telefono">
              <Translate contentKey="veterinarySystemApp.veterinario.telefono">Telefono</Translate>
            </span>
          </dt>
          <dd>{veterinarioEntity.telefono}</dd>
          <dt>
            <span id="especilidad">
              <Translate contentKey="veterinarySystemApp.veterinario.especilidad">Especilidad</Translate>
            </span>
          </dt>
          <dd>{veterinarioEntity.especilidad}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.veterinario.estudios">Estudios</Translate>
          </dt>
          <dd>
            {veterinarioEntity.estudios
              ? veterinarioEntity.estudios.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {veterinarioEntity.estudios && i === veterinarioEntity.estudios.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/veterinario" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/veterinario/${veterinarioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VeterinarioDetail;
