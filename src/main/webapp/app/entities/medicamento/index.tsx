import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Medicamento from './medicamento';
import MedicamentoDetail from './medicamento-detail';
import MedicamentoUpdate from './medicamento-update';
import MedicamentoDeleteDialog from './medicamento-delete-dialog';

const MedicamentoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Medicamento />} />
    <Route path="new" element={<MedicamentoUpdate />} />
    <Route path=":id">
      <Route index element={<MedicamentoDetail />} />
      <Route path="edit" element={<MedicamentoUpdate />} />
      <Route path="delete" element={<MedicamentoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MedicamentoRoutes;
