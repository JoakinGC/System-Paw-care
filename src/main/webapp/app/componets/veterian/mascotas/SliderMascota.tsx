import React from "react";
import { Swiper, SwiperSlide } from 'swiper/react';

import 'swiper/css';
import 'swiper/css/effect-coverflow';
import 'swiper/css/pagination';
import 'swiper/css/navigation';
import './sliderCita.css';
import FichaMascota from "./FichaMascota";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faArrowRight } from "@fortawesome/free-solid-svg-icons";
import { EffectCoverflow, Pagination, Navigation } from 'swiper/modules';
import dayjs from "dayjs";
import './styleCardMascota.css';

const SliderMascota = ({array,title,classname}) =>{

    return(
        <div className="container">
            <h1 className="heading">{title}</h1>

            {(array.length===0)?(<FichaMascota
                            classname={classname}
                            citas={[]}
                            dueno={{'id':11}}
                            especie={{id:4}}
                            fechaNacimiento={dayjs()}
                            id={-1}
                            nCarnet={0}
                            raza={{id:62}}
                            urlImg="not-found.jpg"
                            key={0}
                        />):
            <Swiper
                effect="coverflow"
                grabCursor={true}
                centeredSlides={true}
                loop={true}
                slidesPerView={'auto'}
                coverflowEffect={{
                    rotate:0,
                    stretch:0,
                    depth:100,
                    modifier:2.5,
                }}
                pagination={{el:'.swiper-pagination',clickable:true}}
                navigation={{
                    nextEl:'.swiper-button-next',
                    prevEl:'.swiper-button-prev',
                }}
                modules={[EffectCoverflow,Pagination,Navigation]}
                className="swiper_container"
            >

                {(array)?array.map((mascota,index) =>{
                    return(
                        <SwiperSlide key={index}>
                            <FichaMascota 
                                classname={classname}
                                key={index} 
                                id={mascota.id}
                                dueno={mascota.dueno}
                                especie={mascota.especie}
                                fechaNacimiento={mascota.fechaNacimiento}
                                nCarnet={mascota.nIdentificacionCarnet}
                                raza={mascota.raza}
                                urlImg={mascota.foto}
                                citas={mascota.citas}
                            />
                        </SwiperSlide> 
                    )
                }):(<>
                    <SwiperSlide>
                    <FichaMascota
                            classname={classname}
                            citas={[]}
                            dueno={{'id':11}}
                            especie={{id:4}}
                            fechaNacimiento={dayjs()}
                            id={-1}
                            nCarnet={0}
                            raza={{id:62}}
                            urlImg="not-found.jpg"
                            key={0}
                        />
                    </SwiperSlide>
                    </>
                )}

                <div className="slider-controler">
                    <div className="swiper-button-prev slider-arrow">
                        <FontAwesomeIcon icon={faArrowLeft}/>
                    </div>
                    <div className="swiper-button-next slider-arrow">
                        <FontAwesomeIcon icon={faArrowRight}/>
                    </div>
                    <div className="swiper-pagination"></div>
                </div>
            </Swiper>

            }
        </div>
    )
}

export default SliderMascota;