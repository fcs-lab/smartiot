import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleService from './vehicle-service';
import VehicleServiceDetail from './vehicle-service-detail';
import VehicleServiceUpdate from './vehicle-service-update';
import VehicleServiceDeleteDialog from './vehicle-service-delete-dialog';

const VehicleServiceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleService />} />
    <Route path="new" element={<VehicleServiceUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleServiceDetail />} />
      <Route path="edit" element={<VehicleServiceUpdate />} />
      <Route path="delete" element={<VehicleServiceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleServiceRoutes;
