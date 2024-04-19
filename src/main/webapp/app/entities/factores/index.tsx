import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Factores from './factores';
import FactoresDetail from './factores-detail';
import FactoresUpdate from './factores-update';
import FactoresDeleteDialog from './factores-delete-dialog';

const FactoresRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Factores />} />
    <Route path="new" element={<FactoresUpdate />} />
    <Route path=":id">
      <Route index element={<FactoresDetail />} />
      <Route path="edit" element={<FactoresUpdate />} />
      <Route path="delete" element={<FactoresDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FactoresRoutes;
