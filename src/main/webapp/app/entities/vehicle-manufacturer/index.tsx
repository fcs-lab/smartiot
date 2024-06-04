import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleManufacturer from './vehicle-manufacturer';
import VehicleManufacturerDetail from './vehicle-manufacturer-detail';
import VehicleManufacturerUpdate from './vehicle-manufacturer-update';
import VehicleManufacturerDeleteDialog from './vehicle-manufacturer-delete-dialog';

const VehicleManufacturerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleManufacturer />} />
    <Route path="new" element={<VehicleManufacturerUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleManufacturerDetail />} />
      <Route path="edit" element={<VehicleManufacturerUpdate />} />
      <Route path="delete" element={<VehicleManufacturerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleManufacturerRoutes;
