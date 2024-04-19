import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Especie from './especie';
import EspecieDetail from './especie-detail';
import EspecieUpdate from './especie-update';
import EspecieDeleteDialog from './especie-delete-dialog';

const EspecieRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Especie />} />
    <Route path="new" element={<EspecieUpdate />} />
    <Route path=":id">
      <Route index element={<EspecieDetail />} />
      <Route path="edit" element={<EspecieUpdate />} />
      <Route path="delete" element={<EspecieDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EspecieRoutes;
