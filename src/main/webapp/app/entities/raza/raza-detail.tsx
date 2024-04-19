import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './raza.reducer';

export const RazaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const razaEntity = useAppSelector(state => state.raza.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="razaDetailsHeading">
          <Translate contentKey="veterinarySystemApp.raza.detail.title">Raza</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{razaEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.raza.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{razaEntity.nombre}</dd>
          <dt>
            <span id="nombreCientifico">
              <Translate contentKey="veterinarySystemApp.raza.nombreCientifico">Nombre Cientifico</Translate>
            </span>
          </dt>
          <dd>{razaEntity.nombreCientifico}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.raza.enfermedad">Enfermedad</Translate>
          </dt>
          <dd>
            {razaEntity.enfermedads
              ? razaEntity.enfermedads.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {razaEntity.enfermedads && i === razaEntity.enfermedads.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/raza" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/raza/${razaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RazaDetail;
