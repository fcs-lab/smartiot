import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WaterMeasurement from './water-measurement';
import WaterMeasurementDetail from './water-measurement-detail';
import WaterMeasurementUpdate from './water-measurement-update';
import WaterMeasurementDeleteDialog from './water-measurement-delete-dialog';

const WaterMeasurementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WaterMeasurement />} />
    <Route path="new" element={<WaterMeasurementUpdate />} />
    <Route path=":id">
      <Route index element={<WaterMeasurementDetail />} />
      <Route path="edit" element={<WaterMeasurementUpdate />} />
      <Route path="delete" element={<WaterMeasurementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WaterMeasurementRoutes;
