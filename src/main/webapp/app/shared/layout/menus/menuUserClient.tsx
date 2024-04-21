import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArchive, faStore } from '@fortawesome/free-solid-svg-icons';

export const CorouselMenu = () =>{
  return(
    <>
    <NavItem>
  <NavLink tag={Link} to="/carousel" className="d-flex align-items-center">
    <FontAwesomeIcon icon={faStore} />
    <span>
      <Translate contentKey="global.menu.entities.carousel">Carousel</Translate>
    </span>
  </NavLink>
</NavItem>

    <NavItem>

      <NavLink tag={Link} to="/historial" className="d-flex align-items-center">
        <FontAwesomeIcon icon={faArchive}  />
        <span>
          <Translate contentKey="global.menu.entities.historial">History</Translate>
        </span>
        </NavLink>
    </NavItem>

</>
  );
};

