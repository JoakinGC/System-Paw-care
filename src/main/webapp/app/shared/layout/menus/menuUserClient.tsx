import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArchive,  faCalendarAlt, faCamera,   faGlobe, faPaw, faStore } from '@fortawesome/free-solid-svg-icons';

export const CorouselMenu = () =>{
  const handleNavLinkClick = (navItem) => {
    const link = `http://localhost:5173/`;
    window.location.href = link;
  };

  return(
    <>
    <NavItem>
  <NavLink tag={Link} to="/carousel" className="d-flex align-items-center">
    <FontAwesomeIcon icon={faStore} />
    <span>
      <Translate contentKey="global.menu.entities.corousel">Carousel</Translate>
    </span>
  </NavLink>
</NavItem>

    
    <NavItem>
    <NavLink tag={Link} to="/citasCalendario" className="d-flex align-items-center">
        <FontAwesomeIcon icon={faCalendarAlt}  />
        <span>
          <Translate contentKey="global.menu.entities.cita">History</Translate>
        </span>
        </NavLink>
    </NavItem>

    <NavItem>

      <NavLink tag={Link} to="/historyUser" className="d-flex align-items-center">
        <FontAwesomeIcon icon={faArchive}  />
        <span>
          <Translate contentKey="global.menu.entities.historial">History</Translate>
        </span>
        </NavLink>
    </NavItem>
    
    <NavItem>
  
      <NavLink  className="d-flex align-items-center"  onClick={() => handleNavLinkClick('contenido-de-navitem')}>

        <FontAwesomeIcon icon={faGlobe}  />
        <span>
          <Translate contentKey="global.menu.entities.chat">History</Translate>
        </span>
        </NavLink>
    </NavItem>
    <NavItem>
  
      <NavLink tag={Link} to="/mascotasUser" className="d-flex align-items-center">

        <FontAwesomeIcon icon={faPaw}  />
        <span>
          <Translate contentKey="global.menu.entities.mascota">mascotas</Translate>
        </span>
        </NavLink>
    </NavItem>

    <NavItem>
  
  <NavLink tag={Link} to="/camaraInfo" className="d-flex align-items-center">

    <FontAwesomeIcon icon={faCamera}  />
    <span>
      <Translate contentKey="global.menu.entities.mascota">mascotas</Translate>
    </span>
    </NavLink>
</NavItem>

</>
  );
};

