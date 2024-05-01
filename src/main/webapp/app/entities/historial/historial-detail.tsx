import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './historial.reducer';
import { getEntity as getEnfermedad } from '../enfermedad/enfermedad.reducer';
import { getEntity as getMedicamento } from '../medicamento/medicamento.reducer';

export const HistorialDetail = () => {
  const dispatch = useAppDispatch();
  const [enfermedades, setEnfermedades] = useState<any[]>([])
  const [medicamentos, setMedicamentos] = useState<any[]>([])

  const { id } = useParams<'id'>();
  

  useEffect(() => {
    const fetchHistorialData = async () => {
      // Obtener el historial
      await dispatch(getEntity(id));

  };
  
    fetchHistorialData();
  }, []);

  const historialEntity = useAppSelector(state => state.historial.entity);
  
  useEffect(() => {
    const fetchHistorialData = async () => {
  

      if(historialEntity.enfermedads&&historialEntity.enfermedads.length>1){
        const enfermedadesData = historialEntity.enfermedads.map(async (enfermedad) => {
          const enfermedadData = await dispatch(getEnfermedad(enfermedad.id));
          return (enfermedadData.payload as any).data;
        });
        const enfermedadesResult = await Promise.all(enfermedadesData);
        setEnfermedades(enfermedadesResult);
      }
      
      // Obtener los medicamentos
      if(historialEntity.medicamentos&&historialEntity.medicamentos.length>1){
      const medicamentosData = historialEntity.medicamentos.map(async (medicamento) => {
        const medicamentoData = await dispatch(getMedicamento(medicamento.id));
        return (medicamentoData.payload as any).data;
      });
      const medicamentosResult = await Promise.all(medicamentosData);
      setMedicamentos(medicamentosResult);
    }
  };
  
    fetchHistorialData();
  }, [historialEntity]);
  console.log(historialEntity);
  
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
            {medicamentos
              ? medicamentos.map((val, i) => (
                  <span key={val.id}>
                    <p>{val.nombre}</p>
                    <p>{val.descripcion}</p>
                    {historialEntity.medicamentos && i === historialEntity.medicamentos.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="veterinarySystemApp.historial.enfermedad">Enfermedad</Translate>
          </dt>
          <dd>
            {enfermedades
              ? enfermedades.map((val, i) => (
                
                  <span key={val.id}>
                    <p>{val.nombre}</p>
                    <p>{val.descripcion}</p>
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
        <Button tag={Link} to="/historyUser" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
      </Col>
    </Row>
  );
};

export default HistorialDetail;
