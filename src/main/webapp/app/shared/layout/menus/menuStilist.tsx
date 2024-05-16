import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArchive, faCalendarAlt, faPaw, faShoppingCart, faUserCheck } from '@fortawesome/free-solid-svg-icons';

const MenuStilist = () =>{
    return (
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

      <NavLink tag={Link} to="/datelleCompraVeteAndStil" className="d-flex align-items-center">
        <FontAwesomeIcon icon={faShoppingCart}  />
        <span>
          <Translate contentKey="global.menu.entities.datelleCompra">History</Translate>
        </span>
        </NavLink>
      </NavItem>

        <NavItem>

      <NavLink tag={Link} to="/mascotasVeterian" className="d-flex align-items-center">
      <FontAwesomeIcon icon={faPaw}  />
      <span>
        <Translate contentKey="global.menu.entities.mascota" ></Translate>
        </span>
        </NavLink>
    </NavItem>


        </>
    );
}

export default MenuStilist;