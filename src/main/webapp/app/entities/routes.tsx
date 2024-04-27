import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Terapia from './terapia';
import Factores from './factores';
import Enfermedad from './enfermedad';
import Especie from './especie';
import Raza from './raza';
import Mascota from './mascota';
import Dueno from './dueno';
import Cita from './cita';
import Veterinario from './veterinario';
import Estudios from './estudios';
import Estetica from './estetica';
import CuidadoraHotel from './cuidadora-hotel';
import Historial from './historial';
import Tratamiento from './tratamiento';
import Medicamento from './medicamento';
import Usuario from './usuario';
import Compra from './compra';
import DatelleCompra from './datelle-compra';
import Producto from './producto';
import MenuVeterian from 'app/shared/layout/menus/menuVeterinaria';
import MenuStilist from 'app/shared/layout/menus/menuStilist';
import CorouselRouter from 'app/componets/user/compra/index';
import RoutesCalendarCita from 'app/componets/user/citas';
/* jhipster-needle-add-route-import - JHipster will add routes here */




export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="citasCalendario/*" element={<RoutesCalendarCita />} />
        <Route path="carousel/*" element={<CorouselRouter />} />
        <Route path='veterian/*' element={<MenuVeterian/>}/>
        <Route path='stilist/*' element={<MenuStilist/>}/>
        <Route path="terapia/*" element={<Terapia />} />
        <Route path="factores/*" element={<Factores />} />
        <Route path="enfermedad/*" element={<Enfermedad />} />
        <Route path="especie/*" element={<Especie />} />
        <Route path="raza/*" element={<Raza />} />
        <Route path="mascota/*" element={<Mascota />} />
        <Route path="dueno/*" element={<Dueno />} />
        <Route path="cita/*" element={<Cita />} />
        <Route path="veterinario/*" element={<Veterinario />} />
        <Route path="estudios/*" element={<Estudios />} />
        <Route path="estetica/*" element={<Estetica />} />
        <Route path="cuidadora-hotel/*" element={<CuidadoraHotel />} />
        <Route path="historial/*" element={<Historial />} />
        <Route path="tratamiento/*" element={<Tratamiento />} />
        <Route path="medicamento/*" element={<Medicamento />} />
        <Route path="usuario/*" element={<Usuario />} />
        <Route path="compra/*" element={<Compra />} />
        <Route path="datelle-compra/*" element={<DatelleCompra />} />
        <Route path="producto/*" element={<Producto />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
