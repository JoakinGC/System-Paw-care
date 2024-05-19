import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {  Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity as createNewMedicamento, getEntities as getMedicamentos } from 'app/entities/medicamento/medicamento.reducer';
import { createEntity as createNewEnfermedad, getEntities as getEnfermedads } from 'app/entities/enfermedad/enfermedad.reducer';
import { getEntities as getVeterinarios } from 'app/entities/veterinario/veterinario.reducer';
import { getMascota, getEntities as getMascotas } from 'app/entities/mascota/mascota.reducer';
import { getEntity as getHistorial, updateEntity as updateHistorial, createEntity as createHistorial, reset  as resetHistorial} from '../../../entities/historial/historial.reducer';
import { getEntity as getTratamiento, updateEntity as updateTratamiento, createEntity as createTratamiento, reset as resetTratamientos } from '../../../entities/tratamiento/tratamiento.reducer';
import { getEntities as getHistorials } from 'app/entities/historial/historial.reducer';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import dayjs from 'dayjs';
import { IHistorial } from 'app/shared/model/historial.model';
import { toast } from 'react-toastify';
import { IMedicamento } from 'app/shared/model/medicamento.model';
import { IMascota } from 'app/shared/model/mascota.model';
import { IEnfermedad } from 'app/shared/model/enfermedad.model';

const AddDiagnotsModal = ({veterinario}:

    {veterinario:IVeterinario}
) =>{
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const [newHistorials, setNewHisotrial] = useState<IHistorial>();
  const [parteForm , setParteForm] = useState<number>(1);
  const [medicamentosFiltrados,setMedicamentosFiltrados] = useState<IMedicamento[]>([])
  const [enfermedadesFiltrados,setEnfermedadesFiltrados] = useState<IEnfermedad[]>([])
  const [mascotaFiltrados,setMascotaFiltrados] = useState<IMascota[]>([])

  const medicamentos = useAppSelector(state => state.medicamento.entities);
  const enfermedads = useAppSelector(state => state.enfermedad.entities);
  const mascotas = useAppSelector(state => state.mascota.entities);
  const loadingHistorial = useAppSelector(state => state.historial.loading);
  const updateSuccessHistorial = useAppSelector(state => state.historial.updateSuccess);
  const updateSuccessTratamiento = useAppSelector(state => state.tratamiento.updateSuccess);


  const toggleModal = () =>{
    setParteForm(2)
  }

  const handleClose = () => {
    navigate('/diagnostVeterian' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(resetHistorial());
      dispatch(resetTratamientos());
    } else {
      dispatch(getHistorial(id));
      dispatch(getTratamiento(id));
    }

    dispatch(getMedicamentos({}));
    dispatch(getEnfermedads({}));
    dispatch(getVeterinarios({}));
    dispatch(getMascotas({}));
    dispatch(getHistorials({}));
    
  }, []);

  useEffect(() => {
    if (updateSuccessHistorial) {
      handleClose();
    }
    if (updateSuccessTratamiento) {
      handleClose();
    }
  }, [updateSuccessHistorial,updateSuccessTratamiento]);

  
  const saveEntity = async values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const fechaConsulta = dayjs();
    const entityHistorial:IHistorial = {
      ...newHistorials,
      fechaConsulta,
      veterinario,
    };
        console.log(entityHistorial);
        
      const newHistorial = ((await dispatch(createHistorial(entityHistorial))).payload as any).data;
      console.log("newHisotrila",newHistorial);
      


      setNewHisotrial(newHistorial);
      handleClose();
      /*const entityTratamiento = {
        ...tratamientoEntity,
        ...values,
        newHistorial
      };*/
      //dispatch(createTratamiento(entityTratamiento));
  };

  useEffect(()=>{
    setMascotaFiltrados(mascotas)
    setEnfermedadesFiltrados(enfermedads)
    setMedicamentosFiltrados(medicamentos)
  },[mascotas,medicamentos,enfermedads])

 
   
    const handleSubmitDetalle = (values: any) => {
      if(values.diagnostico===null){
        toast.error("No puede estar vacio")
        return
      }
      if(values.diagnostico.length===0){
        toast.error("No puede estar vacio")
        return
      }
      
      const newH:IHistorial = {
        ...newHistorials,
        'diagnostico':values.diagnostico
      };
      setNewHisotrial(newH);
      setParteForm(3);
      
    };
    console.log(newHistorials);
 
  

    const handleMedicamentoChange = (event) => {
      const searchValue = event.target.value.toLowerCase();
      const filteredMedicamentos = medicamentos.filter(medicamento =>
        medicamento.nombre.toLowerCase().includes(searchValue)
      );
      setMedicamentosFiltrados(filteredMedicamentos);
      console.log(filteredMedicamentos);
    };
    
    const handleSubmitMedicamento = (values) => {
      console.log('Selected Medicamento:', values);
      if(values.medicamentos===null){
        toast.error("NO puede estar vacio")
        return
      }
      if(values.medicamentos.length===0){
        toast.error("NO puede estar vacio")
        return
      }
      
      const nHis:IHistorial = {
        ...newHistorials,
        'medicamentos':mapIdList(values.medicamentos)
      }

      setNewHisotrial(nHis)
      setParteForm(4)
    };
  
    const handleMascotaChange = (event) => {
      const searchValue = event.target.value;
      const filteredMascotas = mascotas.filter(mascota => 
        mascota.nIdentificacionCarnet.toString().includes(searchValue)
      );
      setMascotaFiltrados(filteredMascotas);
      console.log(filteredMascotas);
    };
    
    const onSubmitMascota = async(values) => {
      console.log('Selected Mascota:', values);
      if(values.mascota===null){
        toast.error("NO puede pasar sin seleccionar una mascota");
        return
      }
      if(values.mascota.length===0){
        toast.error("NO puede pasar sin seleccionar una mascota");
        return
      }
      
      const mascotaSeleccionada:IMascota  = await((await dispatch(getMascota(values.mascota))).payload as any).data;
      setNewHisotrial({
        'mascota':mascotaSeleccionada
      })

      console.log(mascotaSeleccionada);
      setParteForm(2)
      

    };
    


    const handleEnfermedadChange = (event) => {
      const searchValue = event.target.value.toLowerCase();
      const filteredEnfermedades = enfermedads.filter(enfermedad =>
        enfermedad.nombre.toLowerCase().includes(searchValue)
      );
      setEnfermedadesFiltrados(filteredEnfermedades);
      console.log(filteredEnfermedades);
    };
  
    const onSubmitEnfermedad = (values) => {
      console.log('Selected Enfermedad:', values);
      if(values.enfermedads===null){
        toast.error("No puede estar vacio")
        return
      }

      if(values.enfermedads.length===0){
        toast.error("No puede estar vacio")
        return
      }
      
      const nH:IHistorial={
        ...newHistorials,
        'enfermedads':mapIdList(values.enfermedads)
      }
      setNewHisotrial(nH);
      setParteForm(5)
    };

   
  

    const saveTratamiento = async(values) =>{
      if (values.id !== undefined && typeof values.id !== 'number') {
        values.id = Number(values.id);
      }
  
      const fechaConsulta = dayjs();
      const entityHistorial:IHistorial = {
        ...newHistorials,
        fechaConsulta,
        veterinario,
      };
          console.log(entityHistorial);
          
        const newHistorial = ((await dispatch(createHistorial(entityHistorial))).payload as any).data;
        console.log("newHisotrila",newHistorial);
      

      const entityTratamiento = {
        ...values,
        newHistorial
      };
      dispatch(createTratamiento(entityTratamiento));
      handleClose();
    }

  const goToAddMedicamento = () => setParteForm(7);
  const addNewTratamiento = () => setParteForm(6)
  const addNewEnfermedad = () => setParteForm(8)

  const onSubmitNewEnfermedad = async(values) =>{

    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...values,
    }

    const newEnfer = await ((await dispatch(createNewEnfermedad(entity))).payload as any).data;
    const hw:IHistorial = {
      ...newHistorials,
      'enfermedads':[newEnfer]
    };

    setNewHisotrial(hw);
    setParteForm(5);
  }

  const onSubmitNewMedicamento = async(values) =>{
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...values,
    };

    const newMedi = await((await dispatch(createNewMedicamento(entity))).payload as any).data;

    const nHis:IHistorial = {
      ...newHistorials,
      'medicamentos':[newMedi]
    }

    setNewHisotrial(nHis)
    setParteForm(4)
  }


  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="veterinarySystemApp.historial.home.createOrEditLabel" data-cy="HistorialCreateUpdateHeading">
            Diagnosticar
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loadingHistorial ? (
            <p>Loading...</p>
          ) : (
            <>

            {(parteForm===1)&&(
              <ValidatedForm onSubmit={onSubmitMascota}>
                <input type='text' placeholder='coloca el nombre del medicamento aqui' 
                  onChange={handleMascotaChange}
                />
                <ValidatedField
                  id="historial-mascota"
                  name="mascota"
                  data-cy="mascota"
                  required={true}
                  label={translate('veterinarySystemApp.historial.mascota')}
                  type="select"
                >
                <option value="" key="0" />
                {mascotaFiltrados
                  ? mascotaFiltrados.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nIdentificacionCarnet }
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button type='submit' >Siguiente</Button>
              <Link to={'/mascotasVeterian'}>
                <Button>Añadir nueva mascota</Button>
              </Link>   
              </ValidatedForm>
            )}

            {(parteForm===2)&&(
              <ValidatedForm onSubmit={handleSubmitDetalle}>
              <ValidatedField
                label={"Datalles importantes a resaltar:"}
                id="historial-diagnostico"
                name="diagnostico"
                data-cy="diagnostico"
                required={true}
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <Button type='submit' >Siguiente</Button>
              </ValidatedForm>
            )}
            {(parteForm===3)&&(
              <ValidatedForm onSubmit={handleSubmitMedicamento}>
                <input type='text' placeholder='coloca el nombre del medicamento aqui' 
                  onChange={handleMedicamentoChange}
                />
              <ValidatedField
                label={"Medicamentos que debe tomar la mascota:"}
                id="historial-medicamento"
                data-cy="medicamento"
                type="select"
                required={true}
                multiple
                name="medicamentos"
              >
                <option value="" key="0" />
                {medicamentosFiltrados
                  ? medicamentosFiltrados.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button type='submit' >Siguiente</Button>
              <Button onClick={goToAddMedicamento}>Añadir un nuevo medicamento</Button>
              </ValidatedForm>
            )}

            {(parteForm===4)&&(
              <ValidatedForm onSubmit={onSubmitEnfermedad}>
                <input type='text' placeholder='coloca el nombre de la enfermedad aqui' 
                  onChange={handleEnfermedadChange}
                />
                <ValidatedField
                label={"Enfermedades diagnosticadas"}
                id="historial-enfermedad"
                data-cy="enfermedad"
                type="select"
                required={true}
                multiple
                name="enfermedads"
              >
                <option value="" key="0" />
                {enfermedadesFiltrados
                  ? enfermedadesFiltrados.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button type='submit'>Siguiente</Button>
              <Button onClick={addNewEnfermedad}>Añadir una enfermedad</Button>
              </ValidatedForm>
            )}

          {(parteForm===5)&&(
              <ValidatedForm onSubmit={saveEntity}>
                
              <Button type='submit' >Siguiente</Button>
              <Button onClick={addNewTratamiento}>Añadir un tratamiento</Button>
              </ValidatedForm>
            )}

            {(parteForm===6)&&(
              <ValidatedForm onSubmit={saveTratamiento}>
                <ValidatedField
                label={`${translate('veterinarySystemApp.tratamiento.fechaInicio')} tratamiento`}
                id="tratamiento-fechaInicio"
                name="fechaInicio"
                data-cy="fechaInicio"
                type="date"
                required={true}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={`${translate('veterinarySystemApp.tratamiento.fechaFin')} tratamiento`}
                id="tratamiento-fechaFin"
                name="fechaFin"
                data-cy="fechaFin"
                type="date"
                required={true}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={`${translate('veterinarySystemApp.tratamiento.notas')} importante`}
                id="tratamiento-notas"
                name="notas"
                data-cy="notas"
                type="text"
                required={true}
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <Button type='submit' >Siguiente</Button>
              </ValidatedForm>
            )}
            {(parteForm===7)&&(
              <ValidatedForm onSubmit={onSubmitNewMedicamento}>
                <ValidatedField
                label={translate('veterinarySystemApp.medicamento.nombre')}
                id="medicamento-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.medicamento.descripcion')}
                id="medicamento-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <Button type='submit' >Siguiente</Button>
              </ValidatedForm>
            )}

            {(parteForm===8)&&(
              <ValidatedForm onSubmit={onSubmitNewEnfermedad}>
                <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.nombre')}
                id="enfermedad-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  maxLength: { value: 20, message: translate('entity.validation.maxlength', { max: 20 }) },
                }}
              />
              <ValidatedField
                label={translate('veterinarySystemApp.enfermedad.descripcion')}
                id="enfermedad-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
                validate={{
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <Button type='submit' >Siguiente</Button>
              </ValidatedForm>
            )}

            </>
          )}
        </Col>
      </Row>
    </div>
  );
}

export default AddDiagnotsModal;
