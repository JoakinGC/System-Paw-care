import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './especie.reducer';

export const EspecieDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const especieEntity = useAppSelector(state => state.especie.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="especieDetailsHeading">
          <Translate contentKey="veterinarySystemApp.especie.detail.title">Especie</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{especieEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.especie.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{especieEntity.nombre}</dd>
          <dt>
            <span id="nombreCientifico">
              <Translate contentKey="veterinarySystemApp.especie.nombreCientifico">Nombre Cientifico</Translate>
            </span>
          </dt>
          <dd>{especieEntity.nombreCientifico}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.especie.enfermedad">Enfermedad</Translate>
          </dt>
          <dd>
            {especieEntity.enfermedads
              ? especieEntity.enfermedads.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {especieEntity.enfermedads && i === especieEntity.enfermedads.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/especie" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/especie/${especieEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EspecieDetail;
