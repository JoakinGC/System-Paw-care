import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import HistoryContainer from './HistoryContainer';




const HistoryUserRouter = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<HistoryContainer />} />
  </ErrorBoundaryRoutes>
);

export default HistoryUserRouter;
