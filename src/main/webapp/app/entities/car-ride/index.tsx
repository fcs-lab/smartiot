import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CarRide from './car-ride';
import CarRideDetail from './car-ride-detail';
import CarRideUpdate from './car-ride-update';
import CarRideDeleteDialog from './car-ride-delete-dialog';

const CarRideRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CarRide />} />
    <Route path="new" element={<CarRideUpdate />} />
    <Route path=":id">
      <Route index element={<CarRideDetail />} />
      <Route path="edit" element={<CarRideUpdate />} />
      <Route path="delete" element={<CarRideDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CarRideRoutes;
