import { faClock } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/mascota/mascota.reducer';
import { IMascota } from 'app/shared/model/mascota.model';
import axios from 'axios';
import { getEntity } from 'app/entities/dueno/dueno.reducer';
import { IDueno } from 'app/shared/model/dueno.model';

interface CardCitaProps {
    id?: number;
    fecha?: string;
    hora?: string;
    mascotas?: IMascota[];
    motivo?: string;
    classname?: string;
  }
  

const CardCita: React.FC<CardCitaProps> = ({ id, fecha, hora, mascotas, motivo, classname }) => {
  const dispatch = useAppDispatch();
  const mascotaList = useAppSelector(state => state.mascota.entities);
  const [mascotasDeLaCita, setMascotasDeLaCita] = useState<IMascota[]>([]);
  const [imageList, setImageList] = useState<string[]>([]);
  const [dueno, setDueno] = useState<IDueno | null>(null);

  useEffect(() => {
    dispatch(getEntities({ page: 0, size: 999, sort: 'id,asc' }));
  }, [dispatch]);

  useEffect(() => {
    if (mascotaList && mascotaList.length > 0) {
      const currentMascotasDeLaCita = mascotaList.filter(mascota => 
        mascota.citas && mascota.citas.some(cita => cita.id === id)
      );
      setMascotasDeLaCita(currentMascotasDeLaCita);
    }
  }, [mascotaList, mascotas]);

  useEffect(() => {
    const fetchImages = async () => {
      if (mascotasDeLaCita && mascotasDeLaCita.length > 0) {
        const currentDueno = ((await dispatch(getEntity(mascotasDeLaCita[0].dueno.id))).payload as any).data;
        setDueno(currentDueno);

        const listImage: string[] = await Promise.all(
          mascotasDeLaCita.map(async (mascota) => {
            try {
              const response = await axios.get(`http://localhost:9000/api/images/${mascota.foto}`, {
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
          })
        );
        setImageList(listImage);
      }
    };
    fetchImages();
  }, [mascotasDeLaCita, dispatch]);

  return (
    <div className={`card ${classname}`}>
      <div className="card-header d-flex justify-content-between align-items-center">
        <div>
          <FontAwesomeIcon icon={faClock} /> {hora}
        </div>
        <div>{fecha}</div>
      </div>
      <div className="card-body">
        <h5 className="card-title text-center">Cita</h5>
        <p className="card-text text-center">{motivo}</p>
        <p className="card-text text-center">{dueno && dueno.nombre} viene con:</p>
        <div className="d-flex">
          {imageList.slice(0, 2).map((imageUrl, i) => (
            <div key={i} className="mr-2">
              <img style={{width:40,height:40}} src={imageUrl} alt="imagen de mascota" className="img-fluid rounded" />
              <p className='text-center'>{mascotasDeLaCita[i].nIdentificacionCarnet}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default CardCita;
