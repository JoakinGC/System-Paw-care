import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './enfermedad.reducer';

export const EnfermedadDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const enfermedadEntity = useAppSelector(state => state.enfermedad.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="enfermedadDetailsHeading">
          <Translate contentKey="veterinarySystemApp.enfermedad.detail.title">Enfermedad</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{enfermedadEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.enfermedad.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{enfermedadEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="veterinarySystemApp.enfermedad.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{enfermedadEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.enfermedad.raza">Raza</Translate>
          </dt>
          <dd>
            {enfermedadEntity.razas
              ? enfermedadEntity.razas.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {enfermedadEntity.razas && i === enfermedadEntity.razas.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.enfermedad.especie">Especie</Translate>
          </dt>
          <dd>
            {enfermedadEntity.especies
              ? enfermedadEntity.especies.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {enfermedadEntity.especies && i === enfermedadEntity.especies.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.enfermedad.terapia">Terapia</Translate>
          </dt>
          <dd>
            {enfermedadEntity.terapias
              ? enfermedadEntity.terapias.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {enfermedadEntity.terapias && i === enfermedadEntity.terapias.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.enfermedad.factores">Factores</Translate>
          </dt>
          <dd>
            {enfermedadEntity.factores
              ? enfermedadEntity.factores.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {enfermedadEntity.factores && i === enfermedadEntity.factores.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.enfermedad.historial">Historial</Translate>
          </dt>
          <dd>
            {enfermedadEntity.historials
              ? enfermedadEntity.historials.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {enfermedadEntity.historials && i === enfermedadEntity.historials.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/enfermedad" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/enfermedad/${enfermedadEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EnfermedadDetail;
