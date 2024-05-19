import React from "react";
import './cardDiagnots.css';
import dayjs from "dayjs";
import { IMascota } from "app/shared/model/mascota.model";
import { IMedicamento } from "app/shared/model/medicamento.model";
import { IEnfermedad } from "app/shared/model/enfermedad.model";
import { IVeterinario } from "app/shared/model/veterinario.model";
import { Button } from "reactstrap";

const CardDiagnots = ({
    id,
    fechaConsulta,
    diagnostico,
    mascota,
    toggleModal
}:{ 
    id:number,
    fechaConsulta:dayjs.Dayjs,
    diagnostico:string
    mascota?: IMascota | null,
    toggleModal:(id:number) =>void,
}) =>{

    const openModal = () =>{
        toggleModal(id)
        console.log("id modal",id);
        
    }


    return(
        <div className="container-card-diagnot">
            <div>
                <h3>Diagnostico</h3>
                <span><strong>Fecha de consulta:</strong>{fechaConsulta&&fechaConsulta.toString()}</span>    
            </div>
            <div className="card-body-diagnot">
                {diagnostico&&diagnostico}
            </div>
            <div className="card-mascota-diagnot">
                <span>
                    n de carnet: 
                </span>
                <span>
                    Raza:                
                </span>
                <span>
                    Especie:
                </span>
            </div>
            <div className="card-mascota-diagnot">
                <span>
                {(mascota&&mascota.nIdentificacionCarnet)?mascota.nIdentificacionCarnet:null }
                </span>
                <span>
                {(mascota&&mascota.raza&&mascota.raza.nombre)?mascota.raza.nombre:null }
                </span>
                <span>
                {(mascota&&mascota.especie&&mascota.especie.nombre)?mascota.especie.nombre:null }
                </span>
            </div>
            <Button onClick={openModal}>Ver detalles</Button>
        </div>
    )
}

export default CardDiagnots