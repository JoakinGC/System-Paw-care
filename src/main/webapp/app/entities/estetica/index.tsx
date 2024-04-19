import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Estetica from './estetica';
import EsteticaDetail from './estetica-detail';
import EsteticaUpdate from './estetica-update';
import EsteticaDeleteDialog from './estetica-delete-dialog';

const EsteticaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Estetica />} />
    <Route path="new" element={<EsteticaUpdate />} />
    <Route path=":id">
      <Route index element={<EsteticaDetail />} />
      <Route path="edit" element={<EsteticaUpdate />} />
      <Route path="delete" element={<EsteticaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EsteticaRoutes;
