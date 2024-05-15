import React from "react";
import './cardCompra.css';
import { Button,Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { IUsuario } from "app/shared/model/usuario.model";
import dayjs from "dayjs";
import { ICompra } from "app/shared/model/compra.model";

const CardCompra = (
    {fecha,total,usuario,toggleModal}:
    {
        id:number,
        fecha:dayjs.Dayjs,
        total:number,
        usuario?: IUsuario | null,
        toggleModal:() => void,
    }
) =>{
    //fecha la compra 
    //total de la compra, y el usuario
    return(<div className="card-compra">
        <div className="card-compra-head">
        <div style={{marginLeft:5}}>
            <h3>Compra</h3>
            <span>{fecha&&fecha.toString()}</span>
        </div>
        <div style={{marginLeft:10}}>
            <span>total:</span>
        </div>
        <div style={{marginLeft:10}}>
            <span>{total&&total}</span>
        </div>
        </div>
        <div className="card-compra-body">
            <div>
                <span>Nombre: {usuario && usuario.dueno && usuario.dueno.nombre ? usuario.dueno.nombre : null}</span>
            </div>
            <div>
                <span>Apellido: {usuario && usuario.dueno && usuario.dueno.apellido ? usuario.dueno.apellido : null}</span>
            </div>
            <div>
                <span>Direccion: {usuario && usuario.dueno && usuario.dueno.direccion ? usuario.dueno.direccion : null}</span>
            </div>
            <div>
                <span>Telefono: {usuario && usuario.dueno && usuario.dueno.telefono ? usuario.dueno.telefono : null}</span>
            </div>
        </div>

        <div className="card-compra-footer">
            <Button>Confirmar entrega de producto</Button>
            <Button onClick={toggleModal}>Detalle de compra</Button>
        </div>
    </div>)
}

export default CardCompra;