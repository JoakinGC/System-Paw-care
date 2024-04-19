import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './estetica.reducer';

export const EsteticaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const esteticaEntity = useAppSelector(state => state.estetica.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="esteticaDetailsHeading">
          <Translate contentKey="veterinarySystemApp.estetica.detail.title">Estetica</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{esteticaEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.estetica.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{esteticaEntity.nombre}</dd>
          <dt>
            <span id="direcion">
              <Translate contentKey="veterinarySystemApp.estetica.direcion">Direcion</Translate>
            </span>
          </dt>
          <dd>{esteticaEntity.direcion}</dd>
          <dt>
            <span id="telefono">
              <Translate contentKey="veterinarySystemApp.estetica.telefono">Telefono</Translate>
            </span>
          </dt>
          <dd>{esteticaEntity.telefono}</dd>
        </dl>
        <Button tag={Link} to="/estetica" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/estetica/${esteticaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EsteticaDetail;
