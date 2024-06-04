import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SystemAlert from './system-alert';
import SystemAlertDetail from './system-alert-detail';
import SystemAlertUpdate from './system-alert-update';
import SystemAlertDeleteDialog from './system-alert-delete-dialog';

const SystemAlertRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SystemAlert />} />
    <Route path="new" element={<SystemAlertUpdate />} />
    <Route path=":id">
      <Route index element={<SystemAlertDetail />} />
      <Route path="edit" element={<SystemAlertUpdate />} />
      <Route path="delete" element={<SystemAlertDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SystemAlertRoutes;
