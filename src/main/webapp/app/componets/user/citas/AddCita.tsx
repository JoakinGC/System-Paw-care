import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getEsteticas } from 'app/entities/estetica/estetica.reducer';
import { getEntities as getCuidadoraHotels } from 'app/entities/cuidadora-hotel/cuidadora-hotel.reducer';
import { getEntities as getVeterinarios } from 'app/entities/veterinario/veterinario.reducer';
import {  getEntities as getMascotas, updateEntity as updateMascota } from 'app/entities/mascota/mascota.reducer';
import { getEntity,  reset, createEntity as createCita, updateEntity as updateCita  } from '../../../entities/cita/cita.reducer';
import { IMascota } from 'app/shared/model/mascota.model';
import dayjs from 'dayjs';

interface PropsAddCita{
    animales:any[];
}

const AddCita = ({toggleModal,selectedDate}) => {
  
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const esteticas = useAppSelector(state => state.estetica.entities);
  const cuidadoraHotels = useAppSelector(state => state.cuidadoraHotel.entities);
  const veterinarios = useAppSelector(state => state.veterinario.entities);
  const mascotas = useAppSelector(state => state.mascota.entities);
  const citaEntity = useAppSelector(state => state.cita.entity);
  const loading = useAppSelector(state => state.cita.loading);
  const updating = useAppSelector(state => state.cita.updating);
  const updateSuccess = useAppSelector(state => state.cita.updateSuccess);
  const mascotasWithCitas = useAppSelector((state) => state.mascotWithCitasReducer.entities);
  const [mascotasSeleccionadas,setMascotasSeleccionadas] = useState<any[]>([]);
  const [isOpen, setIsOpen] = useState(true);

  console.log(mascotasWithCitas);

  const handleClose = () => {
    toggleModal();
  };

  console.log(veterinarios);
  console.log(esteticas);
  

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEsteticas({}));
    dispatch(getCuidadoraHotels({}));
    dispatch(getVeterinarios({}));
    dispatch(getMascotas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = async values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const allMascotas = await dispatch(getMascotas({}));
    const mascotas =(allMascotas.payload as any).data.filter(mascota => values.mascotas.includes(mascota.id.toString())) ;
    console.log("values" , values);
    console.log(selectedDate);
    
    const fecha = dayjs(selectedDate).format('YYYY-MM-DD');;
    console.log(fecha);
    
    const entity = {
      ...citaEntity,
      ...values,
      fecha,
      estetica: esteticas.find(it => it.id.toString() === values.estetica?.toString()),
      cuidadoraHotel: cuidadoraHotels.find(it => it.id.toString() === values.cuidadoraHotel?.toString()),
      veterinario: veterinarios.find(it => it.id.toString() === values.veterinario?.toString()),
      mascotas
    };
    console.log("Entidad: " ,entity);

    
    if (isNew) {
  
      const newEn = await dispatch(createCita(entity));
      console.log(newEn);
      if (newEn.payload) {
      const nuevasMascotas = mascotas.map(mascota => {
        // Clonar la mascota para evitar mutaciones inesperadas
        const nuevaMascota = { ...mascota };
        // Agregar la nueva cita a la lista de citas de la mascota
        nuevaMascota.citas = [...nuevaMascota.citas, (newEn.payload as any).data];
        return nuevaMascota;
      });

      nuevasMascotas.map(async m =>{
        const mas = await dispatch(updateMascota(m));
        console.log("Mascota Actualizada:", mas);
      })
    }
      
    } else {
      
      const newEn = await dispatch(updateCita(entity));
      

      if (newEn.payload) {
      const nuevasMascotas = mascotas.map(mascota => {
        // Clonar la mascota para evitar mutaciones inesperadas
        const nuevaMascota = { ...mascota };
        // Agregar la nueva cita a la lista de citas de la mascota
        nuevaMascota.citas = [...nuevaMascota.citas, (newEn.payload as any).data];
        return nuevaMascota;
      });
      nuevasMascotas.map(async m =>{
        const mas = await dispatch(updateMascota(m));
        console.log("Mascota Actualizada:", mas);
      })        
                      
    }

      
    }
  };


  
  const hadleMascotaSeleccionadas = (mascota: IMascota) => {
    const index = mascotasSeleccionadas.findIndex((m) => m.id === mascota.id);
    if (index === -1) {
      // Si la mascota no está presente, agregarla al array
      setMascotasSeleccionadas([...mascotasSeleccionadas, mascota]);
    } else {
      // Si la mascota ya está presente, eliminarla del array
      const updatedMascotas = [...mascotasSeleccionadas];
      updatedMascotas.splice(index, 1);
      setMascotasSeleccionadas(updatedMascotas);
    }
    console.log("mascotas seleccioandas: ", mascotasSeleccionadas);
    
  };
  

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...citaEntity,
          estetica: citaEntity?.estetica?.id,
          cuidadoraHotel: citaEntity?.cuidadoraHotel?.id,
          veterinario: citaEntity?.veterinario?.id,
          mascotas: citaEntity?.mascotas?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.cita.home.createOrEditLabel" data-cy="CitaCreateUpdateHeading">
            <Translate contentKey="veterinarySystemApp.cita.home.createOrEditLabel">Create or edit a Cita</Translate>
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
                  id="cita-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('veterinarySystemApp.cita.hora')} id="cita-hora" name="hora" data-cy="hora" type="time" />
              <ValidatedField
                label={translate('veterinarySystemApp.cita.motivo')}
                id="cita-motivo"
                name="motivo"
                data-cy="motivo"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                id="cita-estetica"
                name="estetica"
                data-cy="estetica"
                label={translate('veterinarySystemApp.cita.estetica')}
                type="select"
              >
                <option value="" key="0" />
                {esteticas
                  ? esteticas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cita-cuidadoraHotel"
                name="cuidadoraHotel"
                data-cy="cuidadoraHotel"
                label={translate('veterinarySystemApp.cita.cuidadoraHotel')}
                type="select"
              >
                <option value="" key="0" />
                {cuidadoraHotels
                  ? cuidadoraHotels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cita-veterinario"
                name="veterinario"
                data-cy="veterinario"
                label={translate('veterinarySystemApp.cita.veterinario')}
                type="select"
              >
                <option value="" key="0" />
                {veterinarios
                  ? veterinarios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('veterinarySystemApp.cita.mascota')}
                id="cita-mascota"
                data-cy="mascota"
                type="select"
                multiple
                name="mascotas"
              >
                <option value="" key="0" />
                {mascotasWithCitas
                  ? mascotasWithCitas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}
                      onClick={() => hadleMascotaSeleccionadas(otherEntity)}
                      >
                        {otherEntity.nIdentificacionCarnet}
                      </option>
                    ))
                  : null}
              </ValidatedField>
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
    </div>);
};

export default AddCita;
