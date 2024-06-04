import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserDashboard from './user-dashboard';
import UserDashboardDetail from './user-dashboard-detail';
import UserDashboardUpdate from './user-dashboard-update';
import UserDashboardDeleteDialog from './user-dashboard-delete-dialog';

const UserDashboardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserDashboard />} />
    <Route path="new" element={<UserDashboardUpdate />} />
    <Route path=":id">
      <Route index element={<UserDashboardDetail />} />
      <Route path="edit" element={<UserDashboardUpdate />} />
      <Route path="delete" element={<UserDashboardDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserDashboardRoutes;
