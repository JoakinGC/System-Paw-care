import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/terapia">
        <Translate contentKey="global.menu.entities.terapia" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/factores">
        <Translate contentKey="global.menu.entities.factores" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/enfermedad">
        <Translate contentKey="global.menu.entities.enfermedad" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/especie">
        <Translate contentKey="global.menu.entities.especie" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/raza">
        <Translate contentKey="global.menu.entities.raza" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/mascota">
        <Translate contentKey="global.menu.entities.mascota" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dueno">
        <Translate contentKey="global.menu.entities.dueno" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cita">
        <Translate contentKey="global.menu.entities.cita" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/veterinario">
        <Translate contentKey="global.menu.entities.veterinario" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/estudios">
        <Translate contentKey="global.menu.entities.estudios" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/estetica">
        <Translate contentKey="global.menu.entities.estetica" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cuidadora-hotel">
        <Translate contentKey="global.menu.entities.cuidadoraHotel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/historial">
        <Translate contentKey="global.menu.entities.historial" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tratamiento">
        <Translate contentKey="global.menu.entities.tratamiento" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/medicamento">
        <Translate contentKey="global.menu.entities.medicamento" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/usuario">
        <Translate contentKey="global.menu.entities.usuario" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/compra">
        <Translate contentKey="global.menu.entities.compra" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/datelle-compra">
        <Translate contentKey="global.menu.entities.datelleCompra" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/producto">
        <Translate contentKey="global.menu.entities.producto" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
