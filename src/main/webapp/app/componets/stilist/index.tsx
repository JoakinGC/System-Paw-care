import React from 'react';
import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import StylistMain from './StilistMain';

const HistoryUserRouter = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StylistMain />} />
  </ErrorBoundaryRoutes>
);

export default HistoryUserRouter;
