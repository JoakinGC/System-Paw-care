import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './dueno.reducer';

export const DuenoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const duenoEntity = useAppSelector(state => state.dueno.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="duenoDetailsHeading">
          <Translate contentKey="veterinarySystemApp.dueno.detail.title">Dueno</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{duenoEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.dueno.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{duenoEntity.nombre}</dd>
          <dt>
            <span id="apellido">
              <Translate contentKey="veterinarySystemApp.dueno.apellido">Apellido</Translate>
            </span>
          </dt>
          <dd>{duenoEntity.apellido}</dd>
          <dt>
            <span id="direccion">
              <Translate contentKey="veterinarySystemApp.dueno.direccion">Direccion</Translate>
            </span>
          </dt>
          <dd>{duenoEntity.direccion}</dd>
          <dt>
            <span id="telefono">
              <Translate contentKey="veterinarySystemApp.dueno.telefono">Telefono</Translate>
            </span>
          </dt>
          <dd>{duenoEntity.telefono}</dd>
        </dl>
        <Button tag={Link} to="/dueno" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dueno/${duenoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DuenoDetail;
