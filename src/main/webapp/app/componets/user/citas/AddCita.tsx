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
import { getEntities as getAllCitas } from "app/entities/cita/cita.reducer";

interface PropsAddCita{
    animales:any[];
}

const AddCita = ({toggleModal,selectedDate}) => {
  
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const citaList = useAppSelector(state => state.cita.entities);
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

 

  console.log(veterinarios);
  console.log(esteticas);
  

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
    dispatch(getAllCitas({page:0,size:999,sort:`id,asc`}))
    dispatch(getEsteticas({}));
    dispatch(getCuidadoraHotels({}));
    dispatch(getVeterinarios({}));
    dispatch(getMascotas({}));

    
    
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      toggleModal();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = async values => {

    
    const currentDate = new Date();
    if (selectedDate <= currentDate) return
    if(values.hora.length===0) return
    if(values.motivo.length===0) return
    if(values.mascotas.length===0) return
    if(values.estetica.length !=0&&values.veterinario.length!=0) return
    

    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    
   if(values.mascotas.length  >= 3) return
    console.log("citas ",citaList);
    

    const allMascotas = await dispatch(getMascotas({}));
    const mascotas =(allMascotas.payload as any).data.filter(mascota => values.mascotas.includes(mascota.id.toString())) ;
    console.log("values" , values);
    console.log(selectedDate);
    
    let hora = values.hora;
    const fecha = dayjs(selectedDate).format('YYYY-MM-DD');;

    const horaSplit = hora.split(':');
    if (horaSplit.length === 2) {
      hora = `${horaSplit[0]}:${horaSplit[1].padStart(2, '0')}:00`;
    } else if (horaSplit.length === 3) {
      hora = `${horaSplit[0]}:${horaSplit[1].padStart(2, '0')}:${horaSplit[2].padStart(2, '0')}`;
    }

    
    const citaExistente = citaList.find(cita => {
      return cita.fecha == fecha && cita.hora == hora;
    });

    if (citaExistente) {
      console.log("Ya existe una cita en la misma fecha y hora.");
      alert("Ya existe esta cita")
      return;
    }
    
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
        const nuevaMascota = { ...mascota };
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
        const nuevaMascota = { ...mascota };
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
        Pedir una cita
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
          <ValidatedField
            label={translate('veterinarySystemApp.cita.hora')}
            id="cita-hora"
            name="hora"
            data-cy="hora"
            type="time"
            step="3600" // Define el incremento en segundos (3600 segundos = 1 hora)
            min="09:00" // Define la hora mínima permitida
            max="17:00" // Define la hora máxima permitida
          />
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
            label={"Elige estilista que tengas preferencia"}
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
            id="cita-veterinario"
            name="veterinario"
            data-cy="veterinario"
            label={"Elige al veterinario que tengas preferencia"}
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
            label={'¿Cuál de tus mascotas quieres que sea atendida?'}
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
            <Translate contentKey="entity.action.save">Guardar</Translate>
          </Button>
        </ValidatedForm>
      )}
    </Col>
  </Row>
</div>);

};

export default AddCita;
