import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArchive, faCalendarAlt, faShoppingCart, faStore, faUserCheck } from '@fortawesome/free-solid-svg-icons';

const VeterianMenu = () =>{
    

    return(
      <>
      <NavItem>

        <NavLink tag={Link} to="/historial" className="d-flex align-items-center">
          <FontAwesomeIcon icon={faArchive}  />
          <span>
            <Translate contentKey="global.menu.entities.historial">History</Translate>
          </span>
          </NavLink>
      </NavItem>
      <NavItem>

        <NavLink tag={Link} to="/vetirinarioMenu" className="d-flex align-items-center">
        <FontAwesomeIcon icon={faCalendarAlt}  />
        <span>
          <Translate contentKey="global.menu.entities.cita">History</Translate>
        </span>
        </NavLink>
      </NavItem>
      <NavItem>

      <NavLink tag={Link} to="/datelle-compra" className="d-flex align-items-center">
      <FontAwesomeIcon icon={faShoppingCart}  />
      <span>
        <Translate contentKey="global.menu.entities.datelleCompra">History</Translate>
        </span>
        </NavLink>
    </NavItem>
    <NavItem>

      <NavLink tag={Link} to="/vetirinarioMenu/new" className="d-flex align-items-center">
      <FontAwesomeIcon icon={faUserCheck}  />
      <span>
        Diagnostico
        </span>
        </NavLink>
    </NavItem>

  </>
    );
};

export default VeterianMenu;

