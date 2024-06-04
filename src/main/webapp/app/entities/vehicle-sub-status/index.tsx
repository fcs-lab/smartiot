import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleSubStatus from './vehicle-sub-status';
import VehicleSubStatusDetail from './vehicle-sub-status-detail';
import VehicleSubStatusUpdate from './vehicle-sub-status-update';
import VehicleSubStatusDeleteDialog from './vehicle-sub-status-delete-dialog';

const VehicleSubStatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleSubStatus />} />
    <Route path="new" element={<VehicleSubStatusUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleSubStatusDetail />} />
      <Route path="edit" element={<VehicleSubStatusUpdate />} />
      <Route path="delete" element={<VehicleSubStatusDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleSubStatusRoutes;
