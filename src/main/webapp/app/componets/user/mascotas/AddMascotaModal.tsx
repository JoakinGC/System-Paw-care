import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { Modal, ModalHeader, ModalBody, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCitas } from 'app/entities/cita/cita.reducer';
import { getEntities as getDuenos } from 'app/entities/dueno/dueno.reducer';
import { getEntities as getEspecies } from 'app/entities/especie/especie.reducer';
import { getEntities as getRazas } from 'app/entities/raza/raza.reducer';
import { IDueno } from 'app/shared/model/dueno.model';
import { createEntity, getMascota, getEntities as getMascotas, reset, updateEntity} from '../../../entities/mascota/mascota.reducer';
import { useNavigate, useParams } from 'react-router';
import { mapIdList } from 'app/shared/util/entity-utils';

const AddMascotaModal = ({ isOpen, toggle, dueno }: { isOpen: any; toggle: any; dueno: IDueno }) => {
    const dispatch = useAppDispatch();

    const navigate = useNavigate();
  
    const { id } = useParams<'id'>();
    const isNew = id === undefined;
  
    const citas = useAppSelector(state => state.cita.entities);
    const duenos = useAppSelector(state => state.dueno.entities);
    const especies = useAppSelector(state => state.especie.entities);
    const razas = useAppSelector(state => state.raza.entities);
    const mascotaEntity = useAppSelector(state => state.mascota.entity);
    const loading = useAppSelector(state => state.mascota.loading);
    const updating = useAppSelector(state => state.mascota.updating);
    const updateSuccess = useAppSelector(state => state.mascota.updateSuccess);
    

    useEffect(() => {
        if (isNew) {
          dispatch(reset());
        } else {
          dispatch(getMascota(id));
        }
    
        dispatch(getCitas({}));
        dispatch(getDuenos({}));
        dispatch(getEspecies({}));
        dispatch(getRazas({}));
    
        
    
      }, []);

      useEffect(() => {
        if (updateSuccess) {
          toggle();
        }
      }, [updateSuccess]);

      const saveEntity = values => {
        if (values.id !== undefined && typeof values.id !== 'number') {
          values.id = Number(values.id);
        }
        if (values.nIdentificacionCarnet !== undefined && typeof values.nIdentificacionCarnet !== 'number') {
          values.nIdentificacionCarnet = Number(values.nIdentificacionCarnet);
        }
    
    
    
    
        const entity = {
          ...mascotaEntity,
          ...values,
          dueno,
          especie: especies.find(it => it.id.toString() === values.especie?.toString()),
          raza: razas.find(it => it.id.toString() === values.raza?.toString()),
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
          ...mascotaEntity,
          dueno,
          especie: mascotaEntity?.especie?.id,
          raza: mascotaEntity?.raza?.id,
        };

  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle}>Agregar Mascota</ModalHeader>
      <ModalBody>
      
      {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="mascota-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('veterinarySystemApp.mascota.nIdentificacionCarnet')}
                id="mascota-nIdentificacionCarnet"
                name="nIdentificacionCarnet"
                data-cy="nIdentificacionCarnet"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.mascota.foto')}
                id="mascota-foto"
                name="foto"
                data-cy="foto"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />

              <ValidatedField
                label={translate('veterinarySystemApp.mascota.fechaNacimiento')}
                id="mascota-fechaNacimiento"
                name="fechaNacimiento"
                data-cy="fechaNacimiento"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              
              
              <ValidatedField
                id="mascota-especie"
                name="especie"
                data-cy="especie"
                label={translate('veterinarySystemApp.mascota.especie')}
                type="select"
                validate={{
                    required: { value: true, message: "Requiere que eligas una especie" },
                  }}
              >
                <option value="" key="0" />
                {especies
                  ? especies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="mascota-raza"
                name="raza"
                data-cy="raza"
                label={translate('veterinarySystemApp.mascota.raza')}
                type="select"
                validate={{
                    required: { value: true, message: "Requiere que eligas una raza" },
                  }}
              >
                <option value="" key="0" />
                {razas
                  ? razas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
          <Button color="primary" type="submit">
            Guardar
          </Button>
          <Button color="secondary" onClick={toggle}>
            Cancelar
          </Button>
          </ValidatedForm>
            )}
      </ModalBody>
    </Modal>
  );
};

export default AddMascotaModal;
