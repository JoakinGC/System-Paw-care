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
        
  
        const citasConMascota = todasLasCitas.
        filter((ele) =>{
          return (ele.mascotas!=null&&ele.mascotas.length>0)
        })
  
        console.log("Citas con mascota",citasConMascota);
        
        const mascotaConCita = todasLasMascotasYduenos.filter((ele,index) =>{
           return (ele.citas.length>0&&ele.dueno.id===usuarioActual.dueno.id)
        });
        console.log(mascotaConCita);

        const mascotaUsuarioActualCitas = citasConMascota.filter((e,i) =>{
          const mascotasCitaIds = e.mascotas.map(mascota => mascota.id);
          const mascotasConCitaIds = mascotaConCita.map(mascota => mascota.id);

          return mascotasCitaIds.some(id => mascotasConCitaIds.includes(id));
        })

        console.log(mascotaUsuarioActualCitas);
        
        mascotaUsuarioActualCitas.map((e) => console.log(e))
        const formattedEvents = mascotaUsuarioActualCitas.map((cita: any) => ({
          title: 'Cita',
          date: new Date(cita.fecha), // Ajusta cómo obtienes la fecha de la cita
          description: cita.motivo // O cualquier otra información relevante que quieras mostrar
        }));

        console.log(formattedEvents);
        
  
        setEvents(formattedEvents)
      }
      };
      fetchEvents();
    },[allUsarios,todasLasCitas,todasLasMascotasYduenos])

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
  
    const onChange = newDate => {
      setDate(newDate);
      console.log(date);
      setModalOpen(!modalOpen);
    };

    const toggleModal = () => {
      setModalOpen(!modalOpen);
    };
  
    return (
      <div>
        <div style={{ display: 'flex', flexDirection: 'row', alignItems: 'center', justifyContent: 'center', textAlign: 'center' }}>
          <div>
            <h1>Calendario</h1>
            <h3>Selecciona el dia que quieras una cita</h3>
          </div>
          
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