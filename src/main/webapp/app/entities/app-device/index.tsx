import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AppDevice from './app-device';
import AppDeviceDetail from './app-device-detail';
import AppDeviceUpdate from './app-device-update';
import AppDeviceDeleteDialog from './app-device-delete-dialog';

const AppDeviceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AppDevice />} />
    <Route path="new" element={<AppDeviceUpdate />} />
    <Route path=":id">
      <Route index element={<AppDeviceDetail />} />
      <Route path="edit" element={<AppDeviceUpdate />} />
      <Route path="delete" element={<AppDeviceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AppDeviceRoutes;
