import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import VehicleDamage from './vehicle-damage';
import VehicleDamageDetail from './vehicle-damage-detail';
import VehicleDamageUpdate from './vehicle-damage-update';
import VehicleDamageDeleteDialog from './vehicle-damage-delete-dialog';

const VehicleDamageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<VehicleDamage />} />
    <Route path="new" element={<VehicleDamageUpdate />} />
    <Route path=":id">
      <Route index element={<VehicleDamageDetail />} />
      <Route path="edit" element={<VehicleDamageUpdate />} />
      <Route path="delete" element={<VehicleDamageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VehicleDamageRoutes;
