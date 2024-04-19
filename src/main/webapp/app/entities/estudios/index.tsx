import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Estudios from './estudios';
import EstudiosDetail from './estudios-detail';
import EstudiosUpdate from './estudios-update';
import EstudiosDeleteDialog from './estudios-delete-dialog';

const EstudiosRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Estudios />} />
    <Route path="new" element={<EstudiosUpdate />} />
    <Route path=":id">
      <Route index element={<EstudiosDetail />} />
      <Route path="edit" element={<EstudiosUpdate />} />
      <Route path="delete" element={<EstudiosDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EstudiosRoutes;
