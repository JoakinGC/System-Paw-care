import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Corusel from './Corousel';



const CorouselRouter = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Corusel />} />
  </ErrorBoundaryRoutes>
);

export default CorouselRouter;
