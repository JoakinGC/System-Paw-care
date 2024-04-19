import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './estudios.reducer';

export const EstudiosDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const estudiosEntity = useAppSelector(state => state.estudios.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="estudiosDetailsHeading">
          <Translate contentKey="veterinarySystemApp.estudios.detail.title">Estudios</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{estudiosEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="veterinarySystemApp.estudios.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{estudiosEntity.nombre}</dd>
          <dt>
            <span id="fechaCursado">
              <Translate contentKey="veterinarySystemApp.estudios.fechaCursado">Fecha Cursado</Translate>
            </span>
          </dt>
          <dd>
            {estudiosEntity.fechaCursado ? (
              <TextFormat value={estudiosEntity.fechaCursado} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="nombreInsituto">
              <Translate contentKey="veterinarySystemApp.estudios.nombreInsituto">Nombre Insituto</Translate>
            </span>
          </dt>
          <dd>{estudiosEntity.nombreInsituto}</dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.estudios.veterinario">Veterinario</Translate>
          </dt>
          <dd>
            {estudiosEntity.veterinarios
              ? estudiosEntity.veterinarios.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {estudiosEntity.veterinarios && i === estudiosEntity.veterinarios.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/estudios" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/estudios/${estudiosEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EstudiosDetail;
