import React,{useEffect, useState} from "react";
import Calendar from 'react-calendar';
import './styleCalenda.css';
import { AppDispatch } from "app/config/store";
import { useDispatch } from "react-redux";
import { getAccount } from "app/shared/reducers/authentication";
import { getEntities as getUsuarios, getEntity} from 'app/entities/usuario/usuario.reducer';
import { getEntities as getAllCitas } from "app/entities/cita/cita.reducer";
import { getEntities } from "app/entities/mascota/mascota.reducer";
import { Link } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Translate } from "react-jhipster";


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


    //Citas solo para usuario actual
    useEffect(() =>{
      const fetchEvents = async () =>{
        const user = await dispatch(getAccount());
        const { id } = (user.payload as any).data;
        const allUsarios = await dispatch(getUsuarios({}))
        const usuarioActual = (allUsarios.payload as any).data.filter((e,i) => 
        e.user.id==id);
  
        console.log(usuarioActual);
        
  
        const todasLasCitas = await dispatch(getAllCitas({}));
        console.log(todasLasCitas);
  
        const todasLasMascotasYduenos = await dispatch(getEntities({}))
        console.log(todasLasMascotasYduenos);
  
        const citasConMascota = (todasLasCitas.payload as any).data.
        filter((ele) =>{
          return (ele.mascotas!=null&&ele.mascotas.length>0)
        })
  
        console.log("Citas con mascota",citasConMascota);
        
        const mascotaConCita = (todasLasMascotasYduenos.payload as any).data.filter((ele,index) =>{
           return (ele.citas.length>0&&ele.dueno.id===usuarioActual[0].dueno.id)
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
        
      };

      fetchEvents();
    },[])

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
      
    };
  
    return (
      <div>
        <div style={{display:'flex',flexDirection:'row'}}>
          <h1>Calendario</h1>
          <Link to="/citasCalendario/newCita" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="veterinarySystemApp.cita.home.createLabel">Create new Cita</Translate>
          </Link>
        </div>
        <Calendar
          onChange={onChange}
          value={date}
          tileContent={tileContent} 
        />
        
      </div>
    );
  };

export default CitasCalendario;