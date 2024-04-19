import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Compra from './compra';
import CompraDetail from './compra-detail';
import CompraUpdate from './compra-update';
import CompraDeleteDialog from './compra-delete-dialog';

const CompraRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Compra />} />
    <Route path="new" element={<CompraUpdate />} />
    <Route path=":id">
      <Route index element={<CompraDetail />} />
      <Route path="edit" element={<CompraUpdate />} />
      <Route path="delete" element={<CompraDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompraRoutes;
