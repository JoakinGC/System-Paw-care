import React,{useEffect, useState} from "react";
import Calendar from 'react-calendar';
import './styleCalenda.css';
import { AppDispatch, useAppSelector } from "app/config/store";
import { useDispatch, useSelector } from "react-redux";
import { getAccount } from "app/shared/reducers/authentication";
import { getEntities as getUsuarios, getEntity} from 'app/entities/usuario/usuario.reducer';
import { getEntities as getAllCitas } from "app/entities/cita/cita.reducer";
import { getEntities as getAllMascotas} from "app/entities/mascota/mascota.reducer";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Translate } from "react-jhipster";
import { updateMascotasWithCitas } from "./mascotaCita.reducer";
import AddCitaModal from "./AddCitaModal";
import { Button } from "reactstrap";



interface Event {
  title: string;
  date: Date;
  description:string;
  // Otras propiedades de tu evento
}

const CitasCalendario = () =>{
    // Estado para almacenar la fecha seleccionada
    const [date, setDate] = useState(new Date());
    const dispatch = useDispatch<AppDispatch>();
    const [events, setEvents] = useState<Event[]>([]);
    const mascotasWithCitas = useAppSelector((state) => state.mascotWithCitasReducer.entities);
    const allUsarios = useAppSelector(state => state.usuario.entities);
    const todasLasCitas = useAppSelector(state => state.cita.entities);
    const todasLasMascotasYduenos = useAppSelector(state => state.mascota.entities);
    const [modalOpen, setModalOpen] = useState(false);
    const loading = useAppSelector(state => state.cita.loading);
    const [citaFechaPosterior, setCitasFechaPosterior] = useState<any>();
    
    //Citas solo para usuario actual
    useEffect(() =>{
      dispatch(getUsuarios({page:0,size:999,sort:`id,asc`}))
      dispatch(getAllCitas({page:0,size:999,sort:`id,asc`}));
      dispatch(getAllMascotas({page:0,size:999,sort:`id,asc`}))
      
    },[])
    console.log(allUsarios);
    console.log(todasLasCitas);
    console.log(todasLasMascotasYduenos);
    
    useEffect(() =>{
      const fetchEvents = async () =>{

        if (allUsarios.length > 0 && todasLasCitas.length > 0 && todasLasMascotasYduenos.length > 0) {
          const user = await dispatch(getAccount());
          const { id } = (user.payload as any).data;
          const usuarioActual = allUsarios.find((e) => e.user.id === id);

        console.log(usuarioActual);
        
        //mascotas del usuario actual
        const mascotasDelUsuarioActual = todasLasMascotasYduenos
        .filter((e) => e.dueno.id === usuarioActual.dueno.id)
        //este necesito guardarlo en estado grobal usando redux para acceder a la misma varible y tambien poder guardarla
        dispatch(updateMascotasWithCitas(mascotasDelUsuarioActual));

        console.log(mascotasDelUsuarioActual);
        
        
        const mascotaConCita = todasLasMascotasYduenos.filter((ele,index) =>{
          return (ele.citas.length>0&&ele.dueno.id===usuarioActual.dueno.id)
        });
        const citasConMascotaUsuarioActual = todasLasCitas.filter((cita) =>
          mascotaConCita.some((mascota) =>
              mascota.citas.map((mc) => mc.id).includes(cita.id)
          )
        );
      
        console.log("Citas con mascota",citasConMascotaUsuarioActual);


        const currentDateNow = new Date()
        const citasFechaPosterior = citasConMascotaUsuarioActual.filter((e) =>{
          const citaDate = new Date(e.fecha);
          return citaDate >= currentDateNow;
        });

        console.log(citasFechaPosterior);
        
        
        setCitasFechaPosterior(citasFechaPosterior);
        const formattedEvents = citasFechaPosterior.map((cita: any) => ({
          title: 'Cita',
          date: new Date(cita.fecha), 
          description: cita.motivo 
        }));

        console.log(formattedEvents);
        
  
        setEvents(formattedEvents)
      }
      };
      fetchEvents();
    },[allUsarios,todasLasCitas,todasLasMascotasYduenos,modalOpen])

    //citas y que las muestre

    // Boton para genera nueva cita y revisar que haga guarde bien la cita con el id de la mascota

    //Formulario debe mostrar todas las mascotas relacionadas con el usario actual


    //cambiar el backend para que reciba horas

    //mas adelante mostrar los tratamientos y historiales del usario actual con su mascota id 

    // Eventos de ejemplo
    
    
    const tileContent = ({ date, view }: { date: any; view: any }) => {
      if (view === 'month') {
        const eventForDay = events.find((event: any) => event.date.toDateString() === date.toDateString());
        return eventForDay ? (<div>
          <p>{eventForDay.title}</p>
          <p>{eventForDay.description}</p>
          </div>) : null;
      }
    };
  
    const onChange = async newDate => {
      await setDate(newDate);
      console.log(newDate);
    
      const selectedDateString = newDate.toISOString().split('T')[0]; // Obtener la fecha seleccionada en formato de cadena YYYY-MM-DD
      const citaExistente = citaFechaPosterior.find(cita => cita.fecha === selectedDateString);
  
      if (!citaExistente) {
        console.log('Ya existe una cita para este día.');
        return;
      }
    
      setModalOpen(!modalOpen);
    };

    const toggleModal = () => {
      setModalOpen(!modalOpen);
    };
    
    const handleSyncList = () => {
      getAllEntities();
    };

    const getAllEntities = () => {
      dispatch(getUsuarios({page:0,size:999,sort:`id,asc`}))
      dispatch(getAllCitas({page:0,size:999,sort:`id,asc`}));
      dispatch(getAllMascotas({page:0,size:999,sort:`id,asc`}))
    };
    return (
      <div>
        <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'center', textAlign: 'center' }}>
          <div>
            <h1>Calendario</h1>
            <h3>Selecciona el dia que quieras una cita</h3>
          </div>
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            Refrescar Calendario
          </Button>
        </div>

        <div>
        <Calendar
          onChange={onChange}
          value={date}
          tileContent={tileContent} 

        
        />
      
      <AddCitaModal isOpen={modalOpen} toggle={toggleModal} selectedDate={date} />


        </div>
      </div>
    );
  };

export default CitasCalendario;