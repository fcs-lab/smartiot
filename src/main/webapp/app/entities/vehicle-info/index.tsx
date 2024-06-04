import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleInfo from './vehicle-info';
import VehicleInfoDetail from './vehicle-info-detail';
import VehicleInfoUpdate from './vehicle-info-update';
import VehicleInfoDeleteDialog from './vehicle-info-delete-dialog';

const VehicleInfoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleInfo />} />
    <Route path="new" element={<VehicleInfoUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleInfoDetail />} />
      <Route path="edit" element={<VehicleInfoUpdate />} />
      <Route path="delete" element={<VehicleInfoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleInfoRoutes;
