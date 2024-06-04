import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserContract from './user-contract';
import UserContractDetail from './user-contract-detail';
import UserContractUpdate from './user-contract-update';
import UserContractDeleteDialog from './user-contract-delete-dialog';

const UserContractRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserContract />} />
    <Route path="new" element={<UserContractUpdate />} />
    <Route path=":id">
      <Route index element={<UserContractDetail />} />
      <Route path="edit" element={<UserContractUpdate />} />
      <Route path="delete" element={<UserContractDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserContractRoutes;
