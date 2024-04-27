import React,{useState} from "react";
import Calendar from 'react-calendar';
import './styleCalenda.css';

const CitasCalendario = () =>{
    // Estado para almacenar la fecha seleccionada
    const [date, setDate] = useState(new Date());
  


    //Citas solo para usuario actual

    //citas y que las muestre

    // Boton para genera nueva cita y revisar que haga guarde bien la cita con el id de la mascota

    //Formulario debe mostrar todas las mascotas relacionadas con el usario actual


    //cambiar el backend para que reciba horas

    //mas adelante mostrar los tratamientos y historiales del usario actual con su mascota id 

    // Eventos de ejemplo
    const events = [
      { title: 'Evento 1', date: new Date(2024, 3, 20), description: 'Descripción del evento 1' },
      { title: 'Evento 2', date: new Date(2024, 3, 25), description: 'Descripción del evento 2' },
    ];
  
    // Función para verificar si hay eventos en una fecha específica
    const tileContent= ({ date, view }:{date:any,view:any}) => {
      if (view === 'month') {
        const eventForDay = events.find(event => event.date.toDateString() === date.toDateString());
        return eventForDay ? <p>{eventForDay.title}</p> : null;
      }
    };
  
    // Función para manejar el cambio de fecha
    const onChange = newDate => {
      setDate(newDate);
      console.log(date);
      
    };
  
    return (
      <div>
        <h1>Calendario</h1>
        <Calendar
          onChange={onChange}
          value={date}
          tileContent={tileContent} // Agrega el contenido personalizado para los días del calendario
        />
      </div>
    );
  };

export default CitasCalendario;