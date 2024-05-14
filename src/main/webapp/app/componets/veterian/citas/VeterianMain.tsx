import React, { useEffect, useState } from 'react';
import { AppDispatch } from 'app/config/store';
import { useDispatch } from 'react-redux';
import { getAccount } from 'app/shared/reducers/authentication';
import { getEntities as getUsuarios } from 'app/entities/usuario/usuario.reducer';
import { IUser } from 'app/shared/model/user.model';
import { useLocation, useNavigate } from 'react-router';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/cita/cita.reducer';
import { Link } from 'react-router-dom';
import { ICita } from 'app/shared/model/cita.model';
import CardCita from './CardCita';
import SliderCita from './SliderCita';
import ModalAddCita from './ModalAddCita';

const VeterianMain = () =>{
    const [userActual, setUserActual] = useState<IUser|undefined>();
    const dispatch = useDispatch<AppDispatch>();
    const pageLocation = useLocation();
    const navigate = useNavigate();
    const [showModal, setShowModal] = useState(false); // Estado para controlar la visibilidad del modal

    const handleOpenModal = () => setShowModal(true); // Función para abrir el modal
    const handleCloseModal = () => setShowModal(false);// Función para abrir el modal

    const [citasDiaDehoy,setCitasDiaDeHoy] = useState<ICita[]>([]);
    const [citasDiaDeMes,setCitasDiaDeMes] = useState<ICita[]>([]);
    const [citasDiaDeEstaSemana,setCitasDiaDeSmana] = useState<ICita[]>([]);
    const [resultCitasBusqueda,setCitasBuscqueda] =  useState<ICita[]>([]);
    const [todasLasCitasDelVeterinario,setCitasVeterinario] = useState<ICita[]>([])

    const [paginationState, setPaginationState] = useState(
        overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
      );
    
      const citaList = useAppSelector(state => state.cita.entities);
      const loading = useAppSelector(state => state.cita.loading);
      const totalItems = useAppSelector(state => state.cita.totalItems);
      
    
      const searchCita = () =>{
        resultCitasBusqueda
      }
      
      const getAllEntities = () => {
        dispatch(
          getEntities({
            page:0,size:999,sort:`id,asc`
          }),
        );
        
      };
    
  

      useEffect(() =>{
        if(userActual&&citaList.length>0){

          const citaFiltradas = citaList.filter((cita) =>{
            console.log(cita);
            
            return  !cita.atendido&&cita.estetica===null&&cita.veterinario&&cita.veterinario.id==userActual[0].id
          })
          setCitasVeterinario(citaFiltradas)
          console.log(citaFiltradas);

        }
      },[citaList,userActual])

      useEffect(() =>{
        if(userActual&&citaList.length>0&&citaList.length >0){

          const citasOrdenadas = [...todasLasCitasDelVeterinario];

          // Ordenar la lista de citas por fecha
          citasOrdenadas.sort((a, b) => {
              const dateA:Date = new Date(a.fecha.toString());
              const dateB:Date = new Date(b.fecha.toString());
              return dateA.getTime() - dateB.getTime();
          });

        const citasHoy = citasOrdenadas.filter((cita: any) => {
          const fechaCita = new Date(cita.fecha);
          const hoy = new Date();
          return fechaCita.toDateString() === hoy.toDateString();
        });
        setCitasDiaDeHoy(citasHoy);
    

        const citasMesActual = citasOrdenadas.filter((cita: any) => {
          const fechaCita = new Date(cita.fecha);
          const mesActual = new Date().getMonth();
          return fechaCita.getMonth() === mesActual;
        });
        setCitasDiaDeMes(citasMesActual);
    

        const citasSemanaActual = citasOrdenadas.filter((cita: any) => {
          const fechaCita = new Date(cita.fecha);
          const hoy = new Date();
          const primerDiaSemana = new Date(hoy.setDate(hoy.getDate() - hoy.getDay()));
          const ultimoDiaSemana = new Date(hoy.setDate(hoy.getDate() - hoy.getDay() + 6));
          return fechaCita >= primerDiaSemana && fechaCita <= ultimoDiaSemana;
        });
        setCitasDiaDeSmana(citasSemanaActual);
    
        console.log("Citas del día de hoy:", citasHoy);
        console.log("Citas del mes actual:", citasMesActual);
        console.log("Citas de esta semana:", citasSemanaActual);

        }
        
      },[todasLasCitasDelVeterinario])

      useEffect(() =>{
        const fetchUser = async() =>{
            const user = await dispatch(getAccount());
            const { id } = (user.payload as any).data;
            const allUsarios = await dispatch(getUsuarios({}));
            const usuarioActual = await (allUsarios.payload as any).data.filter((e,i) => 
            e.user.id === id);  
            
            console.log(usuarioActual);
            setUserActual(usuarioActual);
        }
        getAllEntities();
        fetchUser();

        
    }, []);
      

    console.log(userActual);
    console.log(citaList);

    return (
        <>
        <div className="container text-center">
            <h1>
                {userActual ? (
                    <> 
                        Bienvenido {userActual[0].nombreUsuario}
                    </>
                ) : (
                    <></>
                )}
            </h1>
            <Button variant="primary" onClick={handleOpenModal} disabled={!userActual}>
              Agregar Cita
            </Button>

        </div>
        <div>
        <SliderCita array={citasDiaDehoy} classname={'cita-hoy'} title={'Citas de hoy'}/>
        <SliderCita array={citasDiaDeEstaSemana} classname={'cita-semana'} title={'Citas de esta semana'}/>
        <SliderCita array={citasDiaDeMes} classname={'cita-mes'} title={'Citas de este mes'}/>
      </div>
      <ModalAddCita isOpen={showModal} toggle={handleCloseModal} veterinario={userActual&&userActual[0]}/>
    </>
    );
}

export default VeterianMain;
