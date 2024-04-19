import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cuidadora-hotel.reducer';

export const CuidadoraHotelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cuidadoraHotelEntity = useAppSelector(state => state.cuidadoraHotel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cuidadoraHotelDetailsHeading">
          <Translate contentKey="veterinarySystemApp.cuidadoraHotel.detail.title">CuidadoraHotel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cuidadoraHotelEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.cuidadoraHotel.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{cuidadoraHotelEntity.nombre}</dd>
          <dt>
            <span id="direccion">
              <Translate contentKey="veterinarySystemApp.cuidadoraHotel.direccion">Direccion</Translate>
            </span>
          </dt>
          <dd>{cuidadoraHotelEntity.direccion}</dd>
          <dt>
            <span id="telefono">
              <Translate contentKey="veterinarySystemApp.cuidadoraHotel.telefono">Telefono</Translate>
            </span>
          </dt>
          <dd>{cuidadoraHotelEntity.telefono}</dd>
          <dt>
            <span id="servicioOfrecido">
              <Translate contentKey="veterinarySystemApp.cuidadoraHotel.servicioOfrecido">Servicio Ofrecido</Translate>
            </span>
          </dt>
          <dd>{cuidadoraHotelEntity.servicioOfrecido}</dd>
        </dl>
        <Button tag={Link} to="/cuidadora-hotel" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cuidadora-hotel/${cuidadoraHotelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CuidadoraHotelDetail;
