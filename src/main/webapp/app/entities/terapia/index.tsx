import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Terapia from './terapia';
import TerapiaDetail from './terapia-detail';
import TerapiaUpdate from './terapia-update';
import TerapiaDeleteDialog from './terapia-delete-dialog';

const TerapiaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Terapia />} />
    <Route path="new" element={<TerapiaUpdate />} />
    <Route path=":id">
      <Route index element={<TerapiaDetail />} />
      <Route path="edit" element={<TerapiaUpdate />} />
      <Route path="delete" element={<TerapiaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TerapiaRoutes;
