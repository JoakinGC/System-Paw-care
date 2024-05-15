import React, { useState, useEffect } from "react";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";
import CardCompra from "./CardCompra";
import CardDetalleCompra from "./CardDetalleCompra";
import dayjs from "dayjs";
import { IUsuario } from "app/shared/model/usuario.model";

const DetalleCompraVeterianAndEstilis = () => {
    const [modalOpen, setModalOpen] = useState(false);

    const toggleModal = () => {
        setModalOpen(!modalOpen);
    };

    const p = dayjs();
    const usuarioPrueba: IUsuario = {
        dueno: {
            apellido: 'pepepona',
            direccion: 'Santa Paola',
            id: 1,
            nombre: 'pepe',
            telefono: "384832",
        },
        nombreUsuario: 'pepe'
    };

    return (
        <>
            <h1>Pepe</h1>
            <div className="container-cards">
            <CardCompra
                fecha={p}
                total={1000}
                key={0}
                usuario={usuarioPrueba}
                toggleModal={toggleModal}
            />
             <CardCompra
                fecha={p}
                total={1000}
                key={0}
                usuario={usuarioPrueba}
                toggleModal={toggleModal}
            />
             <CardCompra
                fecha={p}
                total={1000}
                key={0}
                usuario={usuarioPrueba}
                toggleModal={toggleModal}
            />
             <CardCompra
                fecha={p}
                total={1000}
                key={0}
                usuario={usuarioPrueba}
                toggleModal={toggleModal}
            />
             <CardCompra
                fecha={p}
                total={1000}
                key={0}
                usuario={usuarioPrueba}
                toggleModal={toggleModal}
            />
             <CardCompra
                fecha={p}
                total={1000}
                key={0}
                usuario={usuarioPrueba}
                toggleModal={toggleModal}
            />
             <CardCompra
                fecha={p}
                total={1000}
                key={0}
                usuario={usuarioPrueba}
                toggleModal={toggleModal}
            />
            
            </div>
           
            <Modal isOpen={modalOpen} toggle={toggleModal}>
                <ModalHeader toggle={toggleModal}>Detalle de compra</ModalHeader>
                <ModalBody>
                    <CardDetalleCompra  />
                </ModalBody>
                <ModalFooter>
                    <Button color="secondary" onClick={toggleModal}>Cerrar</Button>
                </ModalFooter>
            </Modal>
        </>
    );
}

export default DetalleCompraVeterianAndEstilis;
