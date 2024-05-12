import { faClock, faUserClock } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";


const CardCita = () =>{

    return(
        <div>
                <span>
                    <FontAwesomeIcon icon={faClock}/>
                    12:00
                </span>
                <p>Fecha</p>
                <h3>Titulo</h3>
                <p>Motivo</p>
                <p>Pepe viene</p>
                

                {/*Litado de mascota */}

                <div>
                    <img src="#" alt="imagen de mascota"></img>
                    <p>n de carnet</p>
                    <p></p>
                </div>
        </div>
    )
}

export default CardCita;