import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Raza from './raza';
import RazaDetail from './raza-detail';
import RazaUpdate from './raza-update';
import RazaDeleteDialog from './raza-delete-dialog';

const RazaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Raza />} />
    <Route path="new" element={<RazaUpdate />} />
    <Route path=":id">
      <Route index element={<RazaDetail />} />
      <Route path="edit" element={<RazaUpdate />} />
      <Route path="delete" element={<RazaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RazaRoutes;
