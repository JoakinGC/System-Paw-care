import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './terapia.reducer';

export const TerapiaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const terapiaEntity = useAppSelector(state => state.terapia.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="terapiaDetailsHeading">
          <Translate contentKey="veterinarySystemApp.terapia.detail.title">Terapia</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{terapiaEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.terapia.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{terapiaEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="veterinarySystemApp.terapia.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{terapiaEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.terapia.enfermedad">Enfermedad</Translate>
          </dt>
          <dd>
            {terapiaEntity.enfermedads
              ? terapiaEntity.enfermedads.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {terapiaEntity.enfermedads && i === terapiaEntity.enfermedads.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/terapia" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/terapia/${terapiaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TerapiaDetail;
