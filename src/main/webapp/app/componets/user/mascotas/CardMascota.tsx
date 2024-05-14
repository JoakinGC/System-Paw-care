import React, { useEffect, useState } from "react";
import './styleCardMascota.css';
import { useDispatch } from "react-redux";
import { getEntity as getDueno} from "app/entities/dueno/dueno.reducer";
import { AppDispatch } from "app/config/store";
import { IMascota } from "app/shared/model/mascota.model";
import { IDueno } from "app/shared/model/dueno.model";
import { IEspecie } from "app/shared/model/especie.model";
import { IRaza } from "app/shared/model/raza.model";
import { getEntity  as getRaza} from "app/entities/raza/raza.reducer";
import { getEntity as getEspecie} from "app/entities/especie/especie.reducer";
import { ICita } from "app/shared/model/cita.model";
import { getEntity as getCita} from "app/entities/cita/cita.reducer";
import { Dayjs } from "dayjs";
import { Button } from "reactstrap";
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Translate } from "react-jhipster";
import { ITratamiento } from "app/shared/model/tratamiento.model";
import { IMedicamento } from "app/shared/model/medicamento.model";
import axios from "axios";
import EditImageMascota from "./EditImageMascota";
import { faCamera, faTrash } from "@fortawesome/free-solid-svg-icons";

interface PropsCardMascota {
    id:number;
    urlImg: string;
    nCarnet: number;
    fechaNacimiento: Dayjs;
    dueno: any;
    especie: any;
    raza: any;
    citas: any[]
}

const CardMascota = ({ id,urlImg, nCarnet, fechaNacimiento, dueno, especie, raza,citas}: PropsCardMascota) => {
    const dispatch = useDispatch()
    const [duenoMascota,setDuenoMascota] = useState<IDueno>({nombre:""});
    const [especieMacota,setEspecieMacota] = useState<IEspecie|null>(null);
    const [razaMascota,setRazaMascota] = useState<IRaza>();
    const [citasMascota,setCitasMascotas] = useState<ICita[]>(); 
    const [medicamentoMascotas,setMedicamentosMascotas] = useState<IMedicamento[]>(); 
    const [tratamientosMascotas,setTratamientosMascotas] = useState<ITratamiento[]>(); 
    const [imageUrl, setImageUrl] = useState<string | null>(null);
    const [nameImage, setNameImage] = useState<string | null>(null);
    const [modalOpen, setModalOpen] = useState<boolean>(false);

    console.log(dueno.id);
    console.log(especie.id);
    console.log(raza.id);

    useEffect(() => {
      const fetchImage = async () => {
        try {
          const imageUrl = await obtenerImagen(urlImg);
          setImageUrl(imageUrl);
        } catch (error) {
          console.error('Error al obtener la imagen:', error);
        }
      };
      fetchImage();
    }, []);

    const obtenerImagen = async (fileName) => {
      try {
        const response = await axios.get(`http://localhost:9000/api/images/${fileName}`, {
          responseType: 'arraybuffer'
        });

        if (response.status !== 200) {
          throw new Error('Error al obtener la imagen');
        }

        const blob = new Blob([response.data], { type: response.headers['content-type'] });
        const imageUrl = URL.createObjectURL(blob);

        return imageUrl;
      } catch (error) {
        console.error('Error:', error);
        return null;
      }
    };
    
    useEffect(() => {
      const fetchDetailsMascota = async () => {
          try {
              // Obtener la fecha de hoy
              const today = new Date();
              const todayString = today.toISOString().split('T')[0];
  
              const idDue: string = dueno.id;
              const idEspe: string|null = especie.id;
              const idRaza: string = raza.id;
  
              const jefe = await (dispatch as AppDispatch)(getDueno(idDue)); 
              const razaM = await (dispatch as AppDispatch)(getRaza(idRaza));
              let especieM=null
              if(idEspe) especieM = await (dispatch as AppDispatch)(getEspecie(idEspe));
  
              setDuenoMascota((jefe.payload as any).data);
              setRazaMascota((razaM.payload as any).data);
              if(especieM) setEspecieMacota((especieM.payload as any).data);
  
              // Citas
              const citasPromises = citas.map(async c => await(dispatch as AppDispatch)(getCita(c.id)));
              const citasResults = await Promise.all(citasPromises);
              const citasData = citasResults.map(result => (result.payload as any).data);
              
              // Filtrar citas anteriores al día de hoy
              const citasFiltradas = citasData.filter(cita => cita.fecha && cita.fecha >= todayString);
              setCitasMascotas(citasFiltradas);
          } catch (error) {
              console.error("Error: ", error);
          }
      };
      fetchDetailsMascota();
  }, []);
  
    
    console.log();
    

    return (
        <div>
        <div className="container-card">
            <div className="card-image-mascota-container">
              {imageUrl && <img src={imageUrl} alt="Imagen de la mascota"/>}
            </div>
            <div className="card-details">
                <span><strong>Número de Carnet: </strong> {nCarnet}</span>
                <span><strong>Fecha de Nacimiento :</strong> {fechaNacimiento.toString()}</span>
                <span><strong>Dueño: </strong>{duenoMascota&&duenoMascota.nombre}</span>
                <span><strong>Especie: </strong>{especieMacota&&especieMacota.nombre} </span>
                <span><strong>Raza: </strong>{razaMascota&&razaMascota.nombre} </span>
            </div>
            <div className="card-icons-options">
            <Button color="primary" onClick={() => setModalOpen(true)} 
            style={{ borderRadius: '20px', marginBottom:10, height:40,marginRight: 40}}>
                <FontAwesomeIcon icon={faCamera}/> 
            </Button>
            <Button
               onClick={() => (window.location.href = `/mascota/${id}/delete`)}
                color="danger"
                 size="sm"
                data-cy="entityDeleteButton"
                style={{ borderRadius: '20px' ,marginRight: 40,height:40}}
              >
                <FontAwesomeIcon icon={faTrash}  />
              </Button>
            </div>
        </div>
        <div className="container-card-citas">
            <h2>Citas</h2>
            {(citasMascota && citasMascota.length>0)?citasMascota.map((e) =>{
                return(<div className="body-cita">
                    <span><strong>Hora: </strong>{e.hora&&e.hora.toString()}</span>
                    <span><strong>fecha: </strong>{e.fecha&&e.fecha.toString()}</span>
                    <span><strong>motivo: </strong>{e.motivo&&e.motivo}</span>
                </div>)
            })
            :
            <h2 style={{color:'red'}}><strong>NO</strong> tiene citas proximas</h2>    
        }
        </div>
        <EditImageMascota isOpen={modalOpen} toggle={() => setModalOpen(!modalOpen)} imageUrl={urlImg}/>
        </div>
    )
}

export default CardMascota;