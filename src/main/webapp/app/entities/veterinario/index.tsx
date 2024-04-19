import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Veterinario from './veterinario';
import VeterinarioDetail from './veterinario-detail';
import VeterinarioUpdate from './veterinario-update';
import VeterinarioDeleteDialog from './veterinario-delete-dialog';

const VeterinarioRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Veterinario />} />
    <Route path="new" element={<VeterinarioUpdate />} />
    <Route path=":id">
      <Route index element={<VeterinarioDetail />} />
      <Route path="edit" element={<VeterinarioUpdate />} />
      <Route path="delete" element={<VeterinarioDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VeterinarioRoutes;
