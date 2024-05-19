import React from "react";
import { Swiper, SwiperSlide } from 'swiper/react';

import 'swiper/css';
import 'swiper/css/effect-coverflow';
import 'swiper/css/pagination';
import 'swiper/css/navigation';
import './sliderCita.css';
import CardCita from "./CardCita";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowLeft, faArrowRight } from "@fortawesome/free-solid-svg-icons";
import { EffectCoverflow, Pagination, Navigation } from 'swiper/modules';

const SliderCita = ({array,title,classname}) =>{
    //pepe
    return(
        <div className="container">
            <h1 className="heading">{title}</h1>

            {(array.length===0)?(<CardCita
                            classname={classname}
                            fecha={"##/##/####"}
                            hora={"##:##"}
                            mascotas={[]}
                            motivo={"No hay citas"}
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

                {(array)?array.map((cita,index) =>{
                    return(
                        <SwiperSlide key={index}>
                            <CardCita 
                                classname={classname}
                                fecha={cita.fecha}
                                hora={cita.hora}
                                mascotas={cita.mascotas}
                                motivo={cita.motivo}
                            />
                        </SwiperSlide> 
                    )
                }):(<>
                    <SwiperSlide>
                        <CardCita
                            classname={classname}
                            fecha={"##/##/####"}
                            hora={"##:##"}
                            mascotas={"No hay citas"}
                            motivo={"No hay citas"}
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

export default SliderCita;