import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './factores.reducer';

export const FactoresDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const factoresEntity = useAppSelector(state => state.factores.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="factoresDetailsHeading">
          <Translate contentKey="veterinarySystemApp.factores.detail.title">Factores</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{factoresEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.factores.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{factoresEntity.nombre}</dd>
          <dt>
            <span id="tipo">
              <Translate contentKey="veterinarySystemApp.factores.tipo">Tipo</Translate>
            </span>
          </dt>
          <dd>{factoresEntity.tipo}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="veterinarySystemApp.factores.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{factoresEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.factores.enfermedad">Enfermedad</Translate>
          </dt>
          <dd>
            {factoresEntity.enfermedads
              ? factoresEntity.enfermedads.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {factoresEntity.enfermedads && i === factoresEntity.enfermedads.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/factores" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/factores/${factoresEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FactoresDetail;
