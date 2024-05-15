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
import { getEntity as getUsuario} from "app/entities/usuario/usuario.reducer";
import { IDueno } from "app/shared/model/dueno.model";
import { getEntity } from "app/entities/dueno/dueno.reducer";

const DetalleCompraVeterianAndEstilis = () =>{
    const dispatch = useAppDispatch();
    const datelleCompraList = useAppSelector(state => state.datelleCompra.entities);
    const loading = useAppSelector(state => state.datelleCompra.loading);
    const compraList = useAppSelector(state => state.compra.entities);
    const [compraListDatilsUser, setCompraListDatilsUser] = useState<ICompra[]|null>(null);
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
    useEffect(() =>{
        if(compraList && compraList.length > 0){
            const fetchData = async () => {
                const comprasConUsuario = await Promise.all(compraList.map(async (c: ICompra) => {
                    let usuario: IUsuario | null = null;
                    let dueno:IDueno|null = null;
                    if(c.usuario){
                        usuario = ((await dispatch(getUsuario(c.usuario.id))).payload as any).data as IUsuario;
                        if(usuario&&usuario.dueno){
                            dueno = ((await dispatch(getEntity(usuario.dueno.id))).payload as any).data as IDueno;
                        }
                        usuario = {...usuario,dueno};
                    }
                    return {...c, usuario};
                }));
                console.log(comprasConUsuario);
                setCompraListDatilsUser(comprasConUsuario)
            };
            fetchData();
        }
    }, [compraList]);
    
   
    console.log(compraList);
    
    return (
        <>
            <h1>Pepe</h1>
            <div className="container-cards">

                {(compraListDatilsUser&&compraListDatilsUser.length>0)?(

                compraListDatilsUser.map((c:ICompra) =>{
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
