import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CostCenter from './cost-center';
import CostCenterDetail from './cost-center-detail';
import CostCenterUpdate from './cost-center-update';
import CostCenterDeleteDialog from './cost-center-delete-dialog';

const CostCenterRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CostCenter />} />
    <Route path="new" element={<CostCenterUpdate />} />
    <Route path=":id">
      <Route index element={<CostCenterDetail />} />
      <Route path="edit" element={<CostCenterUpdate />} />
      <Route path="delete" element={<CostCenterDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CostCenterRoutes;
