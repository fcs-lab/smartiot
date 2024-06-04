import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleGroup from './vehicle-group';
import VehicleGroupDetail from './vehicle-group-detail';
import VehicleGroupUpdate from './vehicle-group-update';
import VehicleGroupDeleteDialog from './vehicle-group-delete-dialog';

const VehicleGroupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleGroup />} />
    <Route path="new" element={<VehicleGroupUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleGroupDetail />} />
      <Route path="edit" element={<VehicleGroupUpdate />} />
      <Route path="delete" element={<VehicleGroupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleGroupRoutes;
