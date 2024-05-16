import React, { useEffect, useState } from "react";
import CardDiagnots from "./CardDiagnots";
import './cardDiagnots.css';
import dayjs from "dayjs";
import { IMascota } from "app/shared/model/mascota.model";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";
import { useAppDispatch, useAppSelector } from "app/config/store";
import { getEntities as getAllHistoriales} from "app/entities/historial/historial.reducer";
import { IHistorial } from "app/shared/model/historial.model";
import { getAccount } from "app/shared/reducers/authentication";
import { getEntities as getAllUsuarios} from "app/entities/usuario/usuario.reducer";
import { IUser } from "app/shared/model/user.model";
import { IUsuario } from "app/shared/model/usuario.model";
import {getMascota} from "app/entities/mascota/mascota.reducer";
import { getEntity as getRaza} from "app/entities/raza/raza.reducer";
import { getEntity as getEspecie} from "app/entities/especie/especie.reducer";

const DiagnostList = () => {
    const [modalOpen, setModalOpen] = useState(false); 
    const dispatch = useAppDispatch();
    const historialList = useAppSelector(state => state.historial.entities);
    const [historialFiltrado,setHistorialFiltrados] = useState<IHistorial[]|null>(null);
    const [userActual, setUserActual] = useState<IUsuario|undefined>();

    const getAllEntities = () => {
        dispatch(
            getAllHistoriales({
            page:0,size:999,sort:`id,asc`
          }),
        );
      };

    useEffect(() =>{
        getAllEntities()
        const fetchUser = async() =>{
            const user = await dispatch(getAccount());
            const { id } = (user.payload as any).data;
            const allUsarios = await dispatch(getAllUsuarios({page:0,size:999,sort:`id,asc`}));
            const usuarioActual = await (allUsarios.payload as any).data.filter((e,i) => 
            e.user.id === id);  
            
            console.log(usuarioActual);
            setUserActual(usuarioActual[0]);
        }
        getAllEntities();
        fetchUser();
    },[])

    useEffect(() =>{
        if(historialList&&historialList.length>0&&userActual){
            

            let historialVeterinarios = historialList.filter((e:IHistorial) =>{
                console.log(e);
                if(e.veterinario&&userActual.veterinario){
                    return e.veterinario.id == userActual.veterinario.id
                }
            })
            const fetch = async () =>{
                historialVeterinarios = await Promise.all(historialVeterinarios.map(async (e: IHistorial) => {
                    let nMas:IMascota = null;
                    console.log(e.mascota);
                    console.log(e.mascota.id&&e.mascota.id);
                    
                    if (e.mascota && e.mascota.id) {
                        const mascotaData = await dispatch(getMascota(e.mascota.id));
                        nMas = await (mascotaData.payload as any).data as IMascota;

                        if(nMas&&nMas.raza){
                            const razaFilt = await  ((await dispatch(getRaza(nMas.raza.id))).payload as any).data;
                            nMas = {...nMas, 'raza':razaFilt};
                        }

                        if(nMas&&nMas.especie){
                            const espeFilt = await  ((await dispatch(getEspecie(nMas.especie.id))).payload as any).data;
                            nMas = {...nMas, 'especie':espeFilt};
                        }

                        console.log(nMas);  
                    }
                    console.log(nMas);
                    const newHisto = { ...e, 'mascota': nMas};
                    console.log(newHisto);
                    
                    return newHisto;
                }));
                console.log(historialVeterinarios);
                setHistorialFiltrados(historialVeterinarios)
            }

            fetch();
            
        }
    },[historialList,userActual])

    const toggleModal = () => {
        setModalOpen(!modalOpen);
    };

    console.log(historialFiltrado);
    
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
                {(historialFiltrado&&historialFiltrado.length>0)?(
                    historialFiltrado.map((d:IHistorial) =>{
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
