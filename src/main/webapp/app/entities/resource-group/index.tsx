import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ResourceGroup from './resource-group';
import ResourceGroupDetail from './resource-group-detail';
import ResourceGroupUpdate from './resource-group-update';
import ResourceGroupDeleteDialog from './resource-group-delete-dialog';

const ResourceGroupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ResourceGroup />} />
    <Route path="new" element={<ResourceGroupUpdate />} />
    <Route path=":id">
      <Route index element={<ResourceGroupDetail />} />
      <Route path="edit" element={<ResourceGroupUpdate />} />
      <Route path="delete" element={<ResourceGroupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ResourceGroupRoutes;
