import React, { useEffect, useState } from "react";
import './styleCardMascota.css';
import { useDispatch } from "react-redux";
import { getEntity as getDueno } from "app/entities/dueno/dueno.reducer";
import { getEntity as getRaza } from "app/entities/raza/raza.reducer";
import { getEntity as getEspecie } from "app/entities/especie/especie.reducer";
import { getEntity as getCita } from "app/entities/cita/cita.reducer";
import { getEntities as getTratamientos } from "app/entities/tratamiento/tratamiento.reducer";
import { AppDispatch } from "app/config/store";
import { IDueno } from "app/shared/model/dueno.model";
import { IEspecie } from "app/shared/model/especie.model";
import { IRaza } from "app/shared/model/raza.model";
import { ICita } from "app/shared/model/cita.model";
import { ITratamiento } from "app/shared/model/tratamiento.model";
import { Dayjs } from "dayjs";
import { Button } from "reactstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCamera } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import EditImageMascota from "./EditImageMascota";
import { getEntities as getAllHistorial } from "app/entities/historial/historial.reducer";
import { IHistorial } from "app/shared/model/historial.model";

interface PropsCardMascota {
  id: number;
  urlImg: string;
  nCarnet: number;
  fechaNacimiento: Dayjs;
  dueno: any;
  especie: any;
  raza: any;
  citas: any[];
}

const CardMascota = ({ id, urlImg, nCarnet, fechaNacimiento, dueno, especie, raza, citas }: PropsCardMascota) => {
  const dispatch = useDispatch<AppDispatch>();
  const [duenoMascota, setDuenoMascota] = useState<IDueno>({ nombre: "" });
  const [especieMacota, setEspecieMacota] = useState<IEspecie | null>(null);
  const [razaMascota, setRazaMascota] = useState<IRaza>();
  const [citasMascota, setCitasMascotas] = useState<ICita[]>();
  const [tratamientos, setTratamientos] = useState<IHistorial[]>([]);
  const [showTratamientos, setShowTratamientos] = useState<boolean>(false);
  const [showCitas, setShowCitas] = useState<boolean>(false);
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [modalOpen, setModalOpen] = useState<boolean>(false);

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
        const today = new Date();
        const todayString = today.toISOString().split('T')[0];

        const idDue: string = dueno.id;
        const idEspe: string | null = especie.id;
        const idRaza: string = raza.id;

        const jefe = await (dispatch as AppDispatch)(getDueno(idDue));
        const razaM = await (dispatch as AppDispatch)(getRaza(idRaza));
        let especieM = null
        if (idEspe) especieM = await (dispatch as AppDispatch)(getEspecie(idEspe));

        setDuenoMascota((jefe.payload as any).data);
        setRazaMascota((razaM.payload as any).data);
        if (especieM) setEspecieMacota((especieM.payload as any).data);

        const citasPromises = citas.map(async c => await (dispatch as AppDispatch)(getCita(c.id)));
        const citasResults = await Promise.all(citasPromises);
        const citasData = citasResults.map(result => (result.payload as any).data);

        const citasFiltradas = citasData.filter(cita => cita.fecha && cita.fecha >= todayString);
        setCitasMascotas(citasFiltradas);
      } catch (error) {
        console.error("Error: ", error);
      }
    };
    fetchDetailsMascota();
  }, []);

  const fetchTratamientos = async () => {
    try {
      const result = await dispatch(getAllHistorial({ page: 0, size: 40, sort: `id,asc` }));
      if (result.payload) {
        const tratamientosFiltrados = (result.payload as any).
          data.filter((tratamiento) => {

            return tratamiento.mascota && tratamiento.mascota.id === id
          });
        setTratamientos(tratamientosFiltrados);
      }
    } catch (error) {
      console.error("Error al obtener los tratamientos: ", error);
    }
  };

  const handleMostrarTratamientos = () => {
    fetchTratamientos();
    setShowTratamientos(!showTratamientos);
  };

  const handleMostrarCitas = () => {
    setShowCitas(!showCitas);
  };

  return (
    <div className="card-container">
      <div className="card">
        <div className="card-image">
          {imageUrl && <img src={imageUrl} alt="Imagen de la mascota" />}
        </div>
        <div className="card-details">
          <span><strong>Número de Carnet: </strong>{nCarnet}</span>
          <span><strong>Fecha de Nacimiento: </strong>{fechaNacimiento.toString()}</span>
          <span><strong>Dueño: </strong>{duenoMascota && duenoMascota.nombre}</span>
          <span><strong>Especie: </strong>{especieMacota && especieMacota.nombre}</span>
          <span><strong>Raza: </strong>{razaMascota && razaMascota.nombre}</span>
        </div>
        <div className="card-actions">
          <Button color="primary" onClick={() => setModalOpen(true)}>
            <FontAwesomeIcon icon={faCamera} /> Editar Imagen
          </Button>
          <Button color="secondary" onClick={handleMostrarTratamientos}>
            {showTratamientos ? "Ocultar Tratamientos" : "Mostrar Tratamientos"}
          </Button>
          <Button color="secondary" onClick={handleMostrarCitas}>
            {showCitas ? "Ocultar Citas" : "Mostrar Citas"}
          </Button>
        </div>
      </div>
      {showCitas && (
        <div className="card-citas">
          <h2>Citas</h2>
          {(citasMascota && citasMascota.length > 0) ? citasMascota.map((cita, index) => (
            <div className="cita" key={index}>
              <span><strong>Hora: </strong>{cita.hora && cita.hora.toString()}<br /></span>
              <span><strong>Fecha: </strong>{cita.fecha && cita.fecha.toString()}<br /></span>
              <span><strong>Motivo: </strong>{cita.motivo && cita.motivo}<br /></span>
            </div>
          )) : <h2 style={{ color: 'red' }}>NO tiene citas próximas</h2>}
        </div>
      )}
      {showTratamientos && (
        <div className="card-tratamientos">
          <h2>Tratamientos</h2>
          {tratamientos.length > 0 ? tratamientos.map((tratamiento, index) => (
            <div className="tratamiento" key={index}>
              <span><strong>Fecha Consulta: </strong>{tratamiento.fechaConsulta && tratamiento.fechaConsulta.toString()}<br /></span>
              <span><strong>Diagnostico: </strong>{tratamiento.diagnostico && tratamiento.diagnostico}<br /></span>
              <hr />
            </div>
          )) : (
            <span>No hay tratamientos registrados.</span>
          )}
        </div>
      )}
      <EditImageMascota isOpen={modalOpen} toggle={() => setModalOpen(!modalOpen)} imageUrl={urlImg} />
    </div>
  )
}

export default CardMascota;
