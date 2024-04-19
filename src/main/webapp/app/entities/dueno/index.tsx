import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Dueno from './dueno';
import DuenoDetail from './dueno-detail';
import DuenoUpdate from './dueno-update';
import DuenoDeleteDialog from './dueno-delete-dialog';

const DuenoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Dueno />} />
    <Route path="new" element={<DuenoUpdate />} />
    <Route path=":id">
      <Route index element={<DuenoDetail />} />
      <Route path="edit" element={<DuenoUpdate />} />
      <Route path="delete" element={<DuenoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DuenoRoutes;
