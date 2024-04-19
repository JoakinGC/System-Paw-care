import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompra } from 'app/shared/model/compra.model';
import { getEntities as getCompras } from 'app/entities/compra/compra.reducer';
import { IProducto } from 'app/shared/model/producto.model';
import { getEntities as getProductos } from 'app/entities/producto/producto.reducer';
import { IDatelleCompra } from 'app/shared/model/datelle-compra.model';
import { getEntity, updateEntity, createEntity, reset } from './datelle-compra.reducer';

export const DatelleCompraUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const compras = useAppSelector(state => state.compra.entities);
  const productos = useAppSelector(state => state.producto.entities);
  const datelleCompraEntity = useAppSelector(state => state.datelleCompra.entity);
  const loading = useAppSelector(state => state.datelleCompra.loading);
  const updating = useAppSelector(state => state.datelleCompra.updating);
  const updateSuccess = useAppSelector(state => state.datelleCompra.updateSuccess);

  const handleClose = () => {
    navigate('/datelle-compra' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompras({}));
    dispatch(getProductos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.cantidad !== undefined && typeof values.cantidad !== 'number') {
      values.cantidad = Number(values.cantidad);
    }
    if (values.precioUnitario !== undefined && typeof values.precioUnitario !== 'number') {
      values.precioUnitario = Number(values.precioUnitario);
    }
    if (values.totalProducto !== undefined && typeof values.totalProducto !== 'number') {
      values.totalProducto = Number(values.totalProducto);
    }

    const entity = {
      ...datelleCompraEntity,
      ...values,
      compra: compras.find(it => it.id.toString() === values.compra?.toString()),
      producto: productos.find(it => it.id.toString() === values.producto?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...datelleCompraEntity,
          compra: datelleCompraEntity?.compra?.id,
          producto: datelleCompraEntity?.producto?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.datelleCompra.home.createOrEditLabel" data-cy="DatelleCompraCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.datelleCompra.home.createOrEditLabel">Create or edit a DatelleCompra</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="datelle-compra-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.datelleCompra.cantidad')}
                id="datelle-compra-cantidad"
                name="cantidad"
                data-cy="cantidad"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.datelleCompra.precioUnitario')}
                id="datelle-compra-precioUnitario"
                name="precioUnitario"
                data-cy="precioUnitario"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.datelleCompra.totalProducto')}
                id="datelle-compra-totalProducto"
                name="totalProducto"
                data-cy="totalProducto"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="datelle-compra-compra"
                name="compra"
                data-cy="compra"
                label={translate('veterinarySystemApp.datelleCompra.compra')}
                type="select"
              >
                <option value="" key="0" />
                {compras
                  ? compras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="datelle-compra-producto"
                name="producto"
                data-cy="producto"
                label={translate('veterinarySystemApp.datelleCompra.producto')}
                type="select"
              >
                <option value="" key="0" />
                {productos
                  ? productos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/datelle-compra" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DatelleCompraUpdate;
