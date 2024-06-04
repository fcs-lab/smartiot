import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DeviceCommand from './device-command';
import DeviceCommandDetail from './device-command-detail';
import DeviceCommandUpdate from './device-command-update';
import DeviceCommandDeleteDialog from './device-command-delete-dialog';

const DeviceCommandRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DeviceCommand />} />
    <Route path="new" element={<DeviceCommandUpdate />} />
    <Route path=":id">
      <Route index element={<DeviceCommandDetail />} />
      <Route path="edit" element={<DeviceCommandUpdate />} />
      <Route path="delete" element={<DeviceCommandDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DeviceCommandRoutes;
