import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Tratamiento from './tratamiento';
import TratamientoDetail from './tratamiento-detail';
import TratamientoUpdate from './tratamiento-update';
import TratamientoDeleteDialog from './tratamiento-delete-dialog';

const TratamientoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Tratamiento />} />
    <Route path="new" element={<TratamientoUpdate />} />
    <Route path=":id">
      <Route index element={<TratamientoDetail />} />
      <Route path="edit" element={<TratamientoUpdate />} />
      <Route path="delete" element={<TratamientoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TratamientoRoutes;
