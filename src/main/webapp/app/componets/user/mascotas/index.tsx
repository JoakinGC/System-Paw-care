import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import MascotaList from './MascotaList';





const MascotaUserRouter = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MascotaList />} />
  </ErrorBoundaryRoutes>
);

export default MascotaUserRouter;
