import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Enfermedad from './enfermedad';
import EnfermedadDetail from './enfermedad-detail';
import EnfermedadUpdate from './enfermedad-update';
import EnfermedadDeleteDialog from './enfermedad-delete-dialog';

const EnfermedadRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Enfermedad />} />
    <Route path="new" element={<EnfermedadUpdate />} />
    <Route path=":id">
      <Route index element={<EnfermedadDetail />} />
      <Route path="edit" element={<EnfermedadUpdate />} />
      <Route path="delete" element={<EnfermedadDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EnfermedadRoutes;
