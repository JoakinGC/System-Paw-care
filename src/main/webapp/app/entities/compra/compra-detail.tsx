import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './compra.reducer';

export const CompraDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const compraEntity = useAppSelector(state => state.compra.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="compraDetailsHeading">
          <Translate contentKey="veterinarySystemApp.compra.detail.title">Compra</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{compraEntity.id}</dd>
          <dt>
            <span id="fechaCompra">
              <Translate contentKey="veterinarySystemApp.compra.fechaCompra">Fecha Compra</Translate>
            </span>
          </dt>
          <dd>
            {compraEntity.fechaCompra ? <TextFormat value={compraEntity.fechaCompra} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="total">
              <Translate contentKey="veterinarySystemApp.compra.total">Total</Translate>
            </span>
          </dt>
          <dd>{compraEntity.total}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.compra.usuario">Usuario</Translate>
          </dt>
          <dd>{compraEntity.usuario ? compraEntity.usuario.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/compra" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/compra/${compraEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompraDetail;
