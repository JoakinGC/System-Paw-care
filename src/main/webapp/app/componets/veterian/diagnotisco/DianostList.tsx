import React, { useEffect, useState } from "react";
import CardDiagnots from "./CardDiagnots";
import './cardDiagnots.css';
import dayjs from "dayjs";
import { IMascota } from "app/shared/model/mascota.model";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";
import { useAppDispatch, useAppSelector } from "app/config/store";
import { getEntities } from "app/entities/historial/historial.reducer";
import { IHistorial } from "app/shared/model/historial.model";

const DiagnostList = () => {
    const [modalOpen, setModalOpen] = useState(false); 
    const dispatch = useAppDispatch();
    const historialList = useAppSelector(state => state.historial.entities);

    const getAllEntities = () => {
        dispatch(
          getEntities({
            page:0,size:999,sort:`id,asc`
          }),
        );
      };

    useEffect(() =>{
        getAllEntities()
    },[])

    const toggleModal = () => {
        setModalOpen(!modalOpen);
    };

    const d = dayjs();  
    const m:IMascota = {
        id:1,
        citas:null,
        dueno:null,
        especie:{id:1,nombre:"perro",},
        fechaNacimiento:dayjs(),
        nIdentificacionCarnet:29393,
        raza:{id:1,nombre:"Perro buldo dog"}
    };

    return (
        <div>
            <h1>Todos los diagnósticos</h1>
            <div className="cards-container">
                <CardDiagnots
                    fechaConsulta={d}
                    mascota={m}
                    diagnostico="Prueba diagnostico"
                    toggleModal={toggleModal}
                />
                {(historialList&&historialList.length>0)?(
                    historialList.map((d:IHistorial) =>{
                        return(
                            <CardDiagnots
                            fechaConsulta={d.fechaConsulta}
                            diagnostico={d.diagnostico}
                            toggleModal={toggleModal}
                            key={d.id}
                            mascota={d.mascota}/>
                        )
                    })
                ):null}
            </div>
            <Modal isOpen={modalOpen} toggle={toggleModal}>
                <ModalHeader toggle={toggleModal}>Detalles del diagnóstico</ModalHeader>
                <ModalBody>
                    <p>Detalles del diagnóstico aquí...</p>
                </ModalBody>
                <ModalFooter>
                    <Button color="secondary" onClick={toggleModal}>Cerrar</Button>
                </ModalFooter>
            </Modal>
        </div>
    );
}

export default DiagnostList;
