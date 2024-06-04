import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ManualEntry from './manual-entry';
import ManualEntryDetail from './manual-entry-detail';
import ManualEntryUpdate from './manual-entry-update';
import ManualEntryDeleteDialog from './manual-entry-delete-dialog';

const ManualEntryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ManualEntry />} />
    <Route path="new" element={<ManualEntryUpdate />} />
    <Route path=":id">
      <Route index element={<ManualEntryDetail />} />
      <Route path="edit" element={<ManualEntryUpdate />} />
      <Route path="delete" element={<ManualEntryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ManualEntryRoutes;
