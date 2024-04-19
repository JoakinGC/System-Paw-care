import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tratamiento.reducer';

export const TratamientoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tratamientoEntity = useAppSelector(state => state.tratamiento.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tratamientoDetailsHeading">
          <Translate contentKey="veterinarySystemApp.tratamiento.detail.title">Tratamiento</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tratamientoEntity.id}</dd>
          <dt>
            <span id="fechaInicio">
              <Translate contentKey="veterinarySystemApp.tratamiento.fechaInicio">Fecha Inicio</Translate>
            </span>
          </dt>
          <dd>
            {tratamientoEntity.fechaInicio ? (
              <TextFormat value={tratamientoEntity.fechaInicio} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="fechaFin">
              <Translate contentKey="veterinarySystemApp.tratamiento.fechaFin">Fecha Fin</Translate>
            </span>
          </dt>
          <dd>
            {tratamientoEntity.fechaFin ? (
              <TextFormat value={tratamientoEntity.fechaFin} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="notas">
              <Translate contentKey="veterinarySystemApp.tratamiento.notas">Notas</Translate>
            </span>
          </dt>
          <dd>{tratamientoEntity.notas}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.tratamiento.historial">Historial</Translate>
          </dt>
          <dd>{tratamientoEntity.historial ? tratamientoEntity.historial.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tratamiento" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tratamiento/${tratamientoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TratamientoDetail;
