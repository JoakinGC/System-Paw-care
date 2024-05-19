import { faClock } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React, { useEffect, useState } from "react";
import './sliderCita.css';
import { useAppDispatch, useAppSelector } from "app/config/store";
import { getEntities } from "app/entities/mascota/mascota.reducer";
import { IMascota } from "app/shared/model/mascota.model";
import axios from "axios";
import { getEntity } from "app/entities/dueno/dueno.reducer";
import { IDueno } from "app/shared/model/dueno.model";

const CardCita = ({ id,fecha, hora, mascotas, motivo,classname }) => {
    const dispatch = useAppDispatch();
    const mascotaList = useAppSelector(state => state.mascota.entities);
    const [mascotasDeLaCita, setMascotasDeLaCita] = useState<IMascota[]>([]);
    const [imageList, setImageList] = useState<string[]>([]);
    const [dueno,setDueno] = useState<IDueno|null>(null);

    useEffect(() => {
        dispatch(getEntities({ page: 0, size: 999, sort: `id,asc` }))
    }, []);
    console.log(fecha);
    console.log(hora);
    
    console.log("Mascotas actuales ",mascotas);
    

    useEffect(() => {
        if (mascotaList && mascotaList.length > 0) {
            const currentMascotasDeLaCita = mascotaList.filter(mascota => 
                mascota.citas && 
                mascota.citas.some(cita => cita.id === id)
            );
            setMascotasDeLaCita(currentMascotasDeLaCita);
        }
    }, [mascotaList, mascotas]);

    useEffect(() => {
        const fetchImages = async () => {
            if (mascotasDeLaCita && mascotasDeLaCita.length > 0) {
                const currentDueno = ((await dispatch(getEntity(mascotasDeLaCita[0].dueno.id))).payload as any).data;
                setDueno(currentDueno)
                console.log((currentDueno));
                

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
        }
        fetchImages();
    }, [mascotasDeLaCita]);

    return (
        <div className={`cita-card-container ${classname}`}>
            <div className="cita-head">
                <span className="cita-date">
                    <div className="cita-time">
                        <FontAwesomeIcon icon={faClock} />
                        {hora && hora}
                    </div>
                    <p>{fecha && fecha}</p>
                </span>
                <div className="cita-head-body">
                    <h3>Cita</h3>
                    <p>{motivo && motivo}</p>
                </div>
            </div>
            <p className="cita-motivo">{dueno&&dueno.nombre} viene</p>
            {(mascotasDeLaCita && mascotasDeLaCita.length > 0) ? (
                    <div className="body-card-cita">
                        {imageList.slice(0, 2).map((imageUrl, i) => (
                            <div key={i} className="container-animal-card-cita">
                                <img src={imageUrl} alt="imagen de mascota" />
                                <p>{mascotasDeLaCita[i].nIdentificacionCarnet}</p>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="body-card-cita">
                        <div className="container-animal-card-cita">
                         <img src="../../../content/images/cat.jpg" alt="imagen de mascota" />
                         <p>Nueva mascota</p>
                    </div>
                </div>
            )}

        </div>
    )
}

export default CardCita;
