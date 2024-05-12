import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import MascotaListVeterinario from './MascotasListVeterinario';


const MascotaVeterianRouter = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MascotaListVeterinario />} />
  </ErrorBoundaryRoutes>
);

export default MascotaVeterianRouter;
