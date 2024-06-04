import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WaterSensor from './water-sensor';
import WaterSensorDetail from './water-sensor-detail';
import WaterSensorUpdate from './water-sensor-update';
import WaterSensorDeleteDialog from './water-sensor-delete-dialog';

const WaterSensorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WaterSensor />} />
    <Route path="new" element={<WaterSensorUpdate />} />
    <Route path=":id">
      <Route index element={<WaterSensorDetail />} />
      <Route path="edit" element={<WaterSensorUpdate />} />
      <Route path="delete" element={<WaterSensorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WaterSensorRoutes;
