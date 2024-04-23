import './home.scss';
import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import './styles.css';
import CarrouselServicesHome from './CarrouselServicesHome';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div className='content'>
      <div className='panel-home container-fluid'> {/* Agrega la clase container-fluid */}
        <h1 className="display-4">
          <Translate contentKey="home.title">¡Bienvenido, Sistema Veterinario!</Translate>
        </h1>
        <p className="lead">
          <Translate contentKey="home.subtitle">Esta es tu página de inicio</Translate>
        </p>
      </div>
      
      <div className='container-fluid'> {/* Agrega la clase container-fluid */}
        
        <CarrouselServicesHome/>
       
      </div>
      <div className='panel-secondary text-center container-fluid'> {/* Agrega la clase container-fluid */}
        <h2>
          <Translate contentKey="home.title-secondary">
            ¡Cuida a tu mejor amigo con PawCare!
          </Translate>
        </h2>
        <p>
          <Translate contentKey="home.subtitle-secondary">
            Descubre PawCare, el sistema veterinario integral diseñado para garantizar el bienestar de tu mascota.
          </Translate>
        </p>
      </div>
    </div>
  );
};

export default Home;
