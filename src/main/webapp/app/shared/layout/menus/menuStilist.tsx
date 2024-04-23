import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArchive } from '@fortawesome/free-solid-svg-icons';

const MenuStilist = () =>{
    return (
        <NavItem>
  
      <NavLink tag={Link} to="/historial" className="d-flex align-items-center">
        <FontAwesomeIcon icon={faArchive}  />
        <span>
          <Translate contentKey="global.menu.entities.historial">History</Translate>
        </span>
      </NavLink>
    </NavItem>
    );
}

export default MenuStilist;