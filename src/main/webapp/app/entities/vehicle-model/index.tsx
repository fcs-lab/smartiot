import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleModel from './vehicle-model';
import VehicleModelDetail from './vehicle-model-detail';
import VehicleModelUpdate from './vehicle-model-update';
import VehicleModelDeleteDialog from './vehicle-model-delete-dialog';

const VehicleModelRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleModel />} />
    <Route path="new" element={<VehicleModelUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleModelDetail />} />
      <Route path="edit" element={<VehicleModelUpdate />} />
      <Route path="delete" element={<VehicleModelDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleModelRoutes;
