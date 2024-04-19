import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DatelleCompra from './datelle-compra';
import DatelleCompraDetail from './datelle-compra-detail';
import DatelleCompraUpdate from './datelle-compra-update';
import DatelleCompraDeleteDialog from './datelle-compra-delete-dialog';

const DatelleCompraRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DatelleCompra />} />
    <Route path="new" element={<DatelleCompraUpdate />} />
    <Route path=":id">
      <Route index element={<DatelleCompraDetail />} />
      <Route path="edit" element={<DatelleCompraUpdate />} />
      <Route path="delete" element={<DatelleCompraDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DatelleCompraRoutes;
