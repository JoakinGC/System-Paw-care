import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CuidadoraHotel from './cuidadora-hotel';
import CuidadoraHotelDetail from './cuidadora-hotel-detail';
import CuidadoraHotelUpdate from './cuidadora-hotel-update';
import CuidadoraHotelDeleteDialog from './cuidadora-hotel-delete-dialog';

const CuidadoraHotelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CuidadoraHotel />} />
    <Route path="new" element={<CuidadoraHotelUpdate />} />
    <Route path=":id">
      <Route index element={<CuidadoraHotelDetail />} />
      <Route path="edit" element={<CuidadoraHotelUpdate />} />
      <Route path="delete" element={<CuidadoraHotelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CuidadoraHotelRoutes;
