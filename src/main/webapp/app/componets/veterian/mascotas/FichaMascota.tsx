import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { getEntity as getDueno } from "app/entities/dueno/dueno.reducer";
import { AppDispatch } from "app/config/store";
import { IDueno } from "app/shared/model/dueno.model";
import { IEspecie } from "app/shared/model/especie.model";
import { IRaza } from "app/shared/model/raza.model";
import { getEntity as getRaza } from "app/entities/raza/raza.reducer";
import { getEntity as getEspecie } from "app/entities/especie/especie.reducer";
import { ICita } from "app/shared/model/cita.model";
import { getEntity as getCita } from "app/entities/cita/cita.reducer";
import { Dayjs } from "dayjs";
import { Button, Card, CardBody, CardHeader, CardImg, CardText, CardTitle, Col, Row } from "reactstrap";
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencilAlt } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import './styleCardMascota.css';
import { Translate } from "react-jhipster";

interface PropsCardMascota {
  id: number;
  urlImg: string;
  nCarnet: number;
  fechaNacimiento: Dayjs;
  dueno: any;
  especie: any;
  raza: any;
  citas: any[];
  classname?: string;
}

const FichaMascota = ({ id, urlImg, nCarnet, fechaNacimiento, dueno, especie, raza, citas, classname }: PropsCardMascota) => {
  const dispatch = useDispatch();
  const [duenoMascota, setDuenoMascota] = useState<IDueno>({ nombre: "" });
  const [especieMascota, setEspecieMascota] = useState<IEspecie | null>(null);
  const [razaMascota, setRazaMascota] = useState<IRaza>();
  const [citasMascota, setCitasMascotas] = useState<ICita[]>();
  const [imageUrl, setImageUrl] = useState<string | null>(null);

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
  }, [urlImg]);

  const obtenerImagen = async (fileName: string) => {
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

        const jefe = await ((await (dispatch as AppDispatch)(getDueno(idDue))).payload as any).data;
        const razaM = await ((await (dispatch as AppDispatch)(getRaza(idRaza))).payload as any).data;
        let especieM = null;
        if (idEspe) especieM = await (dispatch as AppDispatch)(getEspecie(idEspe));

        setDuenoMascota(jefe);
        setRazaMascota(razaM);
        if (especieM) setEspecieMascota((especieM.payload as any).data);

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
  }, [dispatch, dueno.id, especie.id, raza.id, citas]);

  return (
    <Card className={`container-card ${classname}`}>
      <CardHeader>
        <CardTitle tag="h5">Mascota</CardTitle>
      </CardHeader>
      <CardBody>
        <Row>
          <Col md="4">
            {imageUrl && <CardImg top src={imageUrl} alt="Imagen de la mascota" />}
          </Col>
          <Col md="8">
            <CardText><strong>Número de Carnet:</strong> {nCarnet}</CardText>
            <CardText><strong>Fecha de Nacimiento:</strong> {fechaNacimiento.toString()}</CardText>
            <CardText><strong>Dueño:</strong> {duenoMascota.nombre}</CardText>
            <CardText><strong>Especie:</strong> {especieMascota?.nombre}</CardText>
            <CardText><strong>Raza:</strong> {razaMascota?.nombre}</CardText>
            <Button
              tag={Link}
              to={`/mascota/${id}/edit`}
              color="primary"
              size="sm"
              className="mt-3"
            >
              <FontAwesomeIcon icon={faPencilAlt} />{' '}
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </Button>
          </Col>
        </Row>
        {citasMascota && citasMascota.length > 0 && (
          <div className="container-card-citas mt-3" style={{ maxHeight: '150px', overflowY: 'auto' }}>
            <h5 className="title-citas">Citas</h5>
            {citasMascota.map((cita:any, i) => (
              <div key={i} className="card-citas-body">
                <CardText><strong>Hora:</strong> {cita.hora}</CardText>
                <CardText><strong>Fecha:</strong> {cita.fecha}</CardText>
                <CardText className="end-card-cita"><strong>Motivo:</strong> {cita.motivo}</CardText>
              </div>
            ))}
          </div>
        )}
      </CardBody>
    </Card>
  );
}

export default FichaMascota;
