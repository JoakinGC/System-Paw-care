import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './datelle-compra.reducer';

export const DatelleCompraDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const datelleCompraEntity = useAppSelector(state => state.datelleCompra.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="datelleCompraDetailsHeading">
          <Translate contentKey="veterinarySystemApp.datelleCompra.detail.title">DatelleCompra</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{datelleCompraEntity.id}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="veterinarySystemApp.datelleCompra.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{datelleCompraEntity.cantidad}</dd>
          <dt>
            <span id="precioUnitario">
              <Translate contentKey="veterinarySystemApp.datelleCompra.precioUnitario">Precio Unitario</Translate>
            </span>
          </dt>
          <dd>{datelleCompraEntity.precioUnitario}</dd>
          <dt>
            <span id="totalProducto">
              <Translate contentKey="veterinarySystemApp.datelleCompra.totalProducto">Total Producto</Translate>
            </span>
          </dt>
          <dd>{datelleCompraEntity.totalProducto}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.datelleCompra.compra">Compra</Translate>
          </dt>
          <dd>{datelleCompraEntity.compra ? datelleCompraEntity.compra.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.datelleCompra.producto">Producto</Translate>
          </dt>
          <dd>{datelleCompraEntity.producto ? datelleCompraEntity.producto.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/datelle-compra" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/datelle-compra/${datelleCompraEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DatelleCompraDetail;
