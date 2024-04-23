import './header.scss';

import React, { useEffect, useState } from 'react';
import { Translate, Storage } from 'react-jhipster';
import { Navbar, Nav, NavbarToggler, Collapse } from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';

import { Home, Brand } from './header-components';
import { AdminMenu, EntitiesMenu, AccountMenu, LocaleMenu } from '../menus';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import VeterianMenu from '../menus/veterian';
import {  getEntities, getEntity } from 'app/entities/usuario/usuario.reducer';
import { getAccount} from 'app/shared/reducers/authentication';
import MenuStilist from '../menus/menuStilist';
import { CorouselMenu } from '../menus/menuUserClient';




export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const dispatch = useAppDispatch();
  const [foundUser, setFoundUser] = useState<any>(null);

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

  
  
  
  useEffect(() => {
    const result = async () => {
      if (props.isAuthenticated) {
        const actualUser: any = await dispatch(getAccount());
        const usuariosList: any = await dispatch(getEntities({}));
        
        if (actualUser.payload && 'data' in actualUser.payload) {
          const userId = actualUser.payload.data.id;
          const foundUser = usuariosList.payload.data.find((usuario: any) => {
            if (usuario.user && usuario.user.id) {
              return usuario.user.id === userId;
            }
          });

          setFoundUser(foundUser); 
        }
      }
    };
    
    result();
  }, [props.isAuthenticated]);
  
  

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">
          <Translate contentKey={`global.ribbon.${props.ribbonEnv}`} />
        </a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);


  console.log(foundUser?foundUser:"no encontrado");
  console.log(foundUser);
  
  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div id="app-header">
      {renderDevRibbon()}
      <LoadingBar className="loading-bar" />
      <Navbar data-cy="navbar" dark expand="md" fixed="top" className="jh-navbar">
        <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
        <Brand />
        <Collapse isOpen={menuOpen} navbar>
          <Nav id="header-tabs" className="ms-auto" navbar>
            <Home />
            {foundUser&&props.isAuthenticated&&foundUser.veterinario?(
              <VeterianMenu/>
            ):(
              <></>
            )}

            {foundUser&&props.isAuthenticated&&foundUser.dueno?(
              <CorouselMenu/>
            ):(
              <></>
            )}

            {foundUser&&props.isAuthenticated&&foundUser.estetica?(
              <MenuStilist/>
            ):(
              <></>
            )}



            {props.isAuthenticated && props.isAdmin && <EntitiesMenu />}
            {props.isAuthenticated && props.isAdmin && <AdminMenu showOpenAPI={props.isOpenAPIEnabled} />}
            <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} />
            <AccountMenu isAuthenticated={props.isAuthenticated}  name={foundUser&&foundUser.nombreUsuario||props.isAdmin&&'Root'||''}/>
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
};

export default Header;
