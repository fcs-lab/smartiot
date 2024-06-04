import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WaterUsageLog from './water-usage-log';
import WaterUsageLogDetail from './water-usage-log-detail';
import WaterUsageLogUpdate from './water-usage-log-update';
import WaterUsageLogDeleteDialog from './water-usage-log-delete-dialog';

const WaterUsageLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WaterUsageLog />} />
    <Route path="new" element={<WaterUsageLogUpdate />} />
    <Route path=":id">
      <Route index element={<WaterUsageLogDetail />} />
      <Route path="edit" element={<WaterUsageLogUpdate />} />
      <Route path="delete" element={<WaterUsageLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WaterUsageLogRoutes;
