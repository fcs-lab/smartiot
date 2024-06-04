import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WaterAlert from './water-alert';
import WaterAlertDetail from './water-alert-detail';
import WaterAlertUpdate from './water-alert-update';
import WaterAlertDeleteDialog from './water-alert-delete-dialog';

const WaterAlertRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WaterAlert />} />
    <Route path="new" element={<WaterAlertUpdate />} />
    <Route path=":id">
      <Route index element={<WaterAlertDetail />} />
      <Route path="edit" element={<WaterAlertUpdate />} />
      <Route path="delete" element={<WaterAlertDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WaterAlertRoutes;
