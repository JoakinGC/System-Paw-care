import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './historial.reducer';

export const HistorialDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const historialEntity = useAppSelector(state => state.historial.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="historialDetailsHeading">
          <Translate contentKey="veterinarySystemApp.historial.detail.title">Historial</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{historialEntity.id}</dd>
          <dt>
            <span id="fechaConsulta">
              <Translate contentKey="veterinarySystemApp.historial.fechaConsulta">Fecha Consulta</Translate>
            </span>
          </dt>
          <dd>
            {historialEntity.fechaConsulta ? (
              <TextFormat value={historialEntity.fechaConsulta} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="diagnostico">
              <Translate contentKey="veterinarySystemApp.historial.diagnostico">Diagnostico</Translate>
            </span>
          </dt>
          <dd>{historialEntity.diagnostico}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.historial.medicamento">Medicamento</Translate>
          </dt>
          <dd>
            {historialEntity.medicamentos
              ? historialEntity.medicamentos.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {historialEntity.medicamentos && i === historialEntity.medicamentos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.historial.enfermedad">Enfermedad</Translate>
          </dt>
          <dd>
            {historialEntity.enfermedads
              ? historialEntity.enfermedads.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {historialEntity.enfermedads && i === historialEntity.enfermedads.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.historial.veterinario">Veterinario</Translate>
          </dt>
          <dd>{historialEntity.veterinario ? historialEntity.veterinario.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.historial.mascota">Mascota</Translate>
          </dt>
          <dd>{historialEntity.mascota ? historialEntity.mascota.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/historial" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/historial/${historialEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HistorialDetail;
