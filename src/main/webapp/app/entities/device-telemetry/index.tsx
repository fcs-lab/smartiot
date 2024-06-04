import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DeviceTelemetry from './device-telemetry';
import DeviceTelemetryDetail from './device-telemetry-detail';
import DeviceTelemetryUpdate from './device-telemetry-update';
import DeviceTelemetryDeleteDialog from './device-telemetry-delete-dialog';

const DeviceTelemetryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DeviceTelemetry />} />
    <Route path="new" element={<DeviceTelemetryUpdate />} />
    <Route path=":id">
      <Route index element={<DeviceTelemetryDetail />} />
      <Route path="edit" element={<DeviceTelemetryUpdate />} />
      <Route path="delete" element={<DeviceTelemetryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DeviceTelemetryRoutes;
