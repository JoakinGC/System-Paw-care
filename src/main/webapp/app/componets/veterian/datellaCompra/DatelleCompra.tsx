import React, { useState, useEffect } from "react";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";
import CardCompra from "./CardCompra";
import CardDetalleCompra from "./CardDetalleCompra";
import dayjs from "dayjs";
import { IUsuario } from "app/shared/model/usuario.model";
import { useAppDispatch, useAppSelector } from "app/config/store";
import { getEntities as getAllCompras} from "app/entities/compra/compra.reducer";
import { getEntities as getAllDetalleCompra} from "app/entities/datelle-compra/datelle-compra.reducer";
import { ICompra } from "app/shared/model/compra.model";

const DetalleCompraVeterianAndEstilis = () =>{
    const dispatch = useAppDispatch();
    const datelleCompraList = useAppSelector(state => state.datelleCompra.entities);
    const loading = useAppSelector(state => state.datelleCompra.loading);
    const compraList = useAppSelector(state => state.compra.entities);
    const [modalOpen , closeModal] = useState<boolean>();

    const toggleModal = () =>{
        closeModal(!modalOpen)
    }
    const getAllEntities = () => {
        dispatch(
            getAllDetalleCompra({
            page:0,size:999,sort:`id,asc`
          }),
        );

        dispatch(getAllCompras({page:0,size:999,sort:`id,asc`}));
      };

    const searchCompra = () =>{
        //mas adelante si sobra tiempo
    }
    useEffect(() =>{
        getAllEntities()
    },[])
   
    console.log(compraList);
    
    return (
        <>
            <h1>Pepe</h1>
            <div className="container-cards">

                {(compraList&&compraList.length>0)?(

                    compraList.map((c:ICompra) =>{
                        return(
                            <CardCompra
                            id={c.id}
                            fecha={c.fechaCompra}
                            total={c.total}
                            key={c.id}
                            usuario={c.usuario}
                            toggleModal={toggleModal}
                        /> 
                        )
                    })
                ):<></>}
            
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
