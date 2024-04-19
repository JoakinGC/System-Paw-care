import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cita.reducer';

export const CitaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const citaEntity = useAppSelector(state => state.cita.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="citaDetailsHeading">
          <Translate contentKey="veterinarySystemApp.cita.detail.title">Cita</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{citaEntity.id}</dd>
          <dt>
            <span id="hora">
              <Translate contentKey="veterinarySystemApp.cita.hora">Hora</Translate>
            </span>
          </dt>
          <dd>{citaEntity.hora ? <TextFormat value={citaEntity.hora} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="fecha">
              <Translate contentKey="veterinarySystemApp.cita.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>{citaEntity.fecha ? <TextFormat value={citaEntity.fecha} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="motivo">
              <Translate contentKey="veterinarySystemApp.cita.motivo">Motivo</Translate>
            </span>
          </dt>
          <dd>{citaEntity.motivo}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.cita.estetica">Estetica</Translate>
          </dt>
          <dd>{citaEntity.estetica ? citaEntity.estetica.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.cita.cuidadoraHotel">Cuidadora Hotel</Translate>
          </dt>
          <dd>{citaEntity.cuidadoraHotel ? citaEntity.cuidadoraHotel.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.cita.veterinario">Veterinario</Translate>
          </dt>
          <dd>{citaEntity.veterinario ? citaEntity.veterinario.id : ''}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.cita.mascota">Mascota</Translate>
          </dt>
          <dd>
            {citaEntity.mascotas
              ? citaEntity.mascotas.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {citaEntity.mascotas && i === citaEntity.mascotas.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/cita" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cita/${citaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CitaDetail;
