import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Mascota from './mascota';
import MascotaDetail from './mascota-detail';
import MascotaUpdate from './mascota-update';
import MascotaDeleteDialog from './mascota-delete-dialog';

const MascotaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Mascota />} />
    <Route path="new" element={<MascotaUpdate />} />
    <Route path=":id">
      <Route index element={<MascotaDetail />} />
      <Route path="edit" element={<MascotaUpdate />} />
      <Route path="delete" element={<MascotaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MascotaRoutes;
