import React, { useState, useEffect } from "react";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";
import CardCompra from "./CardCompra";
import ModalCardDetils from "./ModalCardDatils";
import dayjs from "dayjs";
import { IUsuario } from "app/shared/model/usuario.model";
import { useAppDispatch, useAppSelector } from "app/config/store";
import { getEntities as getAllCompras, updateEntity} from "app/entities/compra/compra.reducer";
import { getEntities as getAllDetalleCompra} from "app/entities/datelle-compra/datelle-compra.reducer";
import { ICompra } from "app/shared/model/compra.model";
import { getEntity as getUsuario} from "app/entities/usuario/usuario.reducer";
import { IDueno } from "app/shared/model/dueno.model";
import { getEntity } from "app/entities/dueno/dueno.reducer";

const DetalleCompraVeterianAndEstilis = () =>{
    const dispatch = useAppDispatch();
    const compraList = useAppSelector(state => state.compra.entities);
    const datelleCompraList = useAppSelector(state => state.datelleCompra.entities);
    const [compraListDatilsUser, setCompraListDatilsUser] = useState<ICompra[]|null>(null);
    const [modalOpen , closeModal] = useState<boolean>();
    const [idDatelleCompra,setIdDetalleCompra] = useState<number|undefined>();

    const toggleModalDatils = (id:number) =>{
        setIdDetalleCompra(id)
        closeModal(!modalOpen)
    }

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
    
    const updateCompra = async(compra:any) =>{
        await dispatch(updateEntity(compra))
    }
   
    console.log(compraList);
    
    return (
        <>
        <div style={{display:'flex', alignItems:'center',justifyContent:'center'}}>
            <h1>Entregas</h1>
        </div>
            <h2>No entregados</h2>
            <div className="container-cards">
                {(compraListDatilsUser&&compraListDatilsUser.length>0)?(

                compraListDatilsUser.map((c:any) =>{
                    if(!c.entregado){
                        return(
                            <CardCompra
                            id={c.id}
                            fecha={c.fechaCompra}
                            total={c.total}
                            key={c.id}
                            usuario={c.usuario}
                            entregado={false}
                            toggleModal={toggleModalDatils}
                            updateCompra={updateCompra}
                        /> 
                        )
                        }
                    })
                ):<></>}
            
            </div>
            <h2>Entregados</h2>
            <div className="container-cards">
                
                {(compraListDatilsUser&&compraListDatilsUser.length>0)?(

                compraListDatilsUser.map((c:any) =>{
                    if(c.entregado){
                        return(
                            <CardCompra
                            id={c.id}
                            fecha={c.fechaCompra}
                            total={c.total}
                            key={c.id}
                            usuario={c.usuario}
                            entregado={true}
                            toggleModal={toggleModalDatils}
                        /> 
                        )
                        }
                    })
                ):<></>}
            
            </div>
           
            <Modal isOpen={modalOpen} toggle={toggleModal}>
                <ModalHeader toggle={toggleModal}>Detalle de compra</ModalHeader>
                <ModalBody>
                    <ModalCardDetils datalleCompraList={datelleCompraList} idCompra={idDatelleCompra} />
                </ModalBody>
                <ModalFooter>
                    <Button color="secondary" onClick={toggleModal}>Cerrar</Button>
                </ModalFooter>
            </Modal>
        </>
    );
}

export default DetalleCompraVeterianAndEstilis;
