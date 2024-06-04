import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleStatusLog from './vehicle-status-log';
import VehicleStatusLogDetail from './vehicle-status-log-detail';
import VehicleStatusLogUpdate from './vehicle-status-log-update';
import VehicleStatusLogDeleteDialog from './vehicle-status-log-delete-dialog';

const VehicleStatusLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleStatusLog />} />
    <Route path="new" element={<VehicleStatusLogUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleStatusLogDetail />} />
      <Route path="edit" element={<VehicleStatusLogUpdate />} />
      <Route path="delete" element={<VehicleStatusLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleStatusLogRoutes;
