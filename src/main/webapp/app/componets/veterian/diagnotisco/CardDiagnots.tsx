import React from "react";
import './cardDiagnots.css';

const CardDiagnots = () =>{


    return(
        <div className="container-card-diagnot">
            <div>
            <h3>Diagnostico</h3>
            <span>Fecha de consulta</span>    
            </div>
            
            <div className="card-body-diagnot">
            Diagnostico
            </div>
            <div className="card-mascota-diagnot">
                <span>
                    n de carnet
                </span>
                <span>
                    raza                    
                </span>
                <span>
                    especie
                </span>
            </div>
        </div>
    )
}

export default CardDiagnots