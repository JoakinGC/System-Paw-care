import './home.scss';
import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  

  return (
    <div>
      <div className='panel-home container-fluid'> {/* Agrega la clase container-fluid */}
        <img className='logo-home' src='../../../content/images/logotipo.jpeg' alt="Logo"/> {/* Agrega el atributo alt */}
        <h1 className="display-4">
          <Translate contentKey="home.title">¡Bienvenido, Sistema Veterinario!</Translate>
        </h1>
        <p className="lead">
          <Translate contentKey="home.subtitle">Esta es tu página de inicio</Translate>
        </p>
      </div>
      <div className='panel-secondary text-center container-fluid'> {/* Agrega la clase container-fluid */}
        <p >
          <Translate contentKey="home.title-secondary">
            ¡Cuida a tu mejor amigo con PawCare!
          </Translate>
        </p>
        <p>
          <Translate contentKey="home.subtitle-secondary">
            Descubre PawCare, el sistema veterinario integral diseñado para garantizar el bienestar de tu mascota.
          </Translate>
        </p>
      </div>
      <div className='panel-services container-fluid'> {/* Agrega la clase container-fluid */}
        <h2>Servicios Disponibles:</h2>
        <Row>
          <Col sm="4">
          <div className="card panel-service text-white bg-dark mb-3">
              <h5 className="card-title"><Translate contentKey="home.service1">Veterinarios especialistas</Translate></h5>
            </div>
          </Col>
          <Col sm="4">
          <div className="card panel-service text-white bg-dark mb-3">
              <h5 className="card-title"><Translate contentKey="home.service2">Veterinarios especialistas</Translate></h5>
            </div>
          </Col>
          <Col sm="4">
          <div className="card panel-service text-white bg-dark mb-3">
              <h5 className="card-title"><Translate contentKey="home.service3">Veterinarios especialistas</Translate></h5>
            </div>
          </Col>
        </Row>
        <Row className=''>
          {account?.login ? (
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                ¡Has iniciado sesión como usuario! {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">Si quieres</Translate>
              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> iniciar sesión</Translate>
              </Link>
              <Translate contentKey="global.messages.info.authenticated.suffix">
                , puedes probar con las cuentas predeterminadas:
                <br />- Administrador (login=&quot;admin&quot; y contraseña=&quot;admin&quot;)
                <br />- Usuario (login=&quot;usuario&quot; y contraseña=&quot;usuario&quot;).
              </Translate>
            </Alert>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.register.noaccount">¿Todavía no tienes una cuenta? ¡</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Regístrate para obtener una nueva cuenta</Translate>
              </Link>
            </Alert>
          </div>
        )}
        </Row>
      </div>
    </div>
  );
};

export default Home;
