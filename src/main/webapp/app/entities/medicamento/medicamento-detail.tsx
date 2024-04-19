import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './medicamento.reducer';

export const MedicamentoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const medicamentoEntity = useAppSelector(state => state.medicamento.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="medicamentoDetailsHeading">
          <Translate contentKey="veterinarySystemApp.medicamento.detail.title">Medicamento</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{medicamentoEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.medicamento.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{medicamentoEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="veterinarySystemApp.medicamento.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{medicamentoEntity.descripcion}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.medicamento.historial">Historial</Translate>
          </dt>
          <dd>
            {medicamentoEntity.historials
              ? medicamentoEntity.historials.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {medicamentoEntity.historials && i === medicamentoEntity.historials.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/medicamento" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/medicamento/${medicamentoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MedicamentoDetail;
