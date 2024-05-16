import React from "react";
import CardDiagnots from "./CardDiagnots";
import './cardDiagnots.css';

const DiagnostList = () =>{

    return(
        <div>
            <h1>Todos los diagnósticos</h1>
            <div className="cards-container">
                {/* Aquí se renderizarán las tarjetas */}
                <CardDiagnots/>
                <CardDiagnots/>
                {/* Añade más tarjetas según sea necesario */}
            </div>
        </div>
    )
}

export default DiagnostList;
