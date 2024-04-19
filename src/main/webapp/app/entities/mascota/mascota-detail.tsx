import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './mascota.reducer';

export const MascotaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mascotaEntity = useAppSelector(state => state.mascota.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mascotaDetailsHeading">
          <Translate contentKey="veterinarySystemApp.mascota.detail.title">Mascota</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mascotaEntity.id}</dd>
          <dt>
            <span id="nIdentificacionCarnet">
              <Translate contentKey="veterinarySystemApp.mascota.nIdentificacionCarnet">N Identificacion Carnet</Translate>
            </span>
          </dt>
          <dd>{mascotaEntity.nIdentificacionCarnet}</dd>
          <dt>
            <span id="foto">
              <Translate contentKey="veterinarySystemApp.mascota.foto">Foto</Translate>
            </span>
          </dt>
          <dd>{mascotaEntity.foto}</dd>
          <dt>
            <span id="fechaNacimiento">
              <Translate contentKey="veterinarySystemApp.mascota.fechaNacimiento">Fecha Nacimiento</Translate>
            </span>
          </dt>
          <dd>
            {mascotaEntity.fechaNacimiento ? (
              <TextFormat value={mascotaEntity.fechaNacimiento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.mascota.cita">Cita</Translate>
          </dt>
          <dd>
            {mascotaEntity.citas
              ? mascotaEntity.citas.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {mascotaEntity.citas && i === mascotaEntity.citas.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.mascota.dueno">Dueno</Translate>
          </dt>
          <dd>{mascotaEntity.dueno ? mascotaEntity.dueno.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.mascota.especie">Especie</Translate>
          </dt>
          <dd>{mascotaEntity.especie ? mascotaEntity.especie.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.mascota.raza">Raza</Translate>
          </dt>
          <dd>{mascotaEntity.raza ? mascotaEntity.raza.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/mascota" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mascota/${mascotaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MascotaDetail;
