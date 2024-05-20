import React, { useState } from "react";
import { Translate } from "react-jhipster";
import './corruselStyles.css';

const CarouselServicesHome = () => {
  const [activeIndex, setActiveIndex] = useState(0);

  const handlePrevSlide = () => {
    const newIndex = (activeIndex - 1 + 3) % 3;
    setActiveIndex(newIndex);
  };

  const handleNextSlide = () => {
    const newIndex = (activeIndex + 1) % 3;
    setActiveIndex(newIndex);
  };

  return (
    <div id="carouselExampleDark" className="carousel carousel-dark slide" data-bs-ride="carousel">
      <div className="carousel-indicators">
        <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="0" className={activeIndex === 0 ? "active" : ""} aria-current={activeIndex === 0 ? "true" : "false"} aria-label="Slide 1"></button>
        <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="1" className={activeIndex === 1 ? "active" : ""} aria-current={activeIndex === 1 ? "true" : "false"} aria-label="Slide 2"></button>
        <button type="button" data-bs-target="#carouselExampleDark" data-bs-slide-to="2" className={activeIndex === 2 ? "active" : ""} aria-current={activeIndex === 2 ? "true" : "false"} aria-label="Slide 3"></button>
      </div>
      <div className="carousel-inner" style={{color:'white'}}>
        <div className={activeIndex === 0 ? "carousel-item active" : "carousel-item"} data-bs-interval="10000" style={{color:'white'}}>
          <img src='../../../content/images/horses.jpg' className="d-block w-100 carousel-img" alt="First slide"/>
          <div className="carousel-caption d-none d-md-block caption-bg" style={{color:'white'}}>
            <h5 className="card-title"><Translate contentKey="home.service1">Veterinarios especialistas</Translate></h5>
          </div>
        </div>
        <div className={activeIndex === 1 ? "carousel-item active" : "carousel-item"} data-bs-interval="2000">
          <img src='../../../content/images/cat.jpg' className="d-block w-100 carousel-img" alt="Second slide"/>
          <div className="carousel-caption d-none d-md-block caption-bg" style={{color:'white'}}>
            <h5 className="card-title"><Translate contentKey="home.service2">Veterinarios especialistas</Translate></h5>
          </div>
        </div>
        <div className={activeIndex === 2 ? "carousel-item active" : "carousel-item"}>
          <img src='../../../content/images/fish.jpg' className="d-block w-100 carousel-img" alt="Third slide"/>
          <div className="carousel-caption d-none d-md-block caption-bg " style={{color:'white'}}>
            <h5 className="card-title"><Translate contentKey="home.service3">Veterinarios especialistas</Translate></h5>
          </div>
        </div>
      </div>
      <button className="carousel-control-prev" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="prev" onClick={handlePrevSlide}>
        <span className="carousel-control-prev-icon" aria-hidden="true"></span>
        <span className="visually-hidden">Previous</span>
      </button>
      <button className="carousel-control-next" type="button" data-bs-target="#carouselExampleDark" data-bs-slide="next" onClick={handleNextSlide}>
        <span className="carousel-control-next-icon" aria-hidden="true"></span>
        <span className="visually-hidden">Next</span>
      </button>
    </div>
  );
}

export default CarouselServicesHome;
