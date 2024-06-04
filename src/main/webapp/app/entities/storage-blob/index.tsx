import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StorageBlob from './storage-blob';
import StorageBlobDetail from './storage-blob-detail';
import StorageBlobUpdate from './storage-blob-update';
import StorageBlobDeleteDialog from './storage-blob-delete-dialog';

const StorageBlobRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StorageBlob />} />
    <Route path="new" element={<StorageBlobUpdate />} />
    <Route path=":id">
      <Route index element={<StorageBlobDetail />} />
      <Route path="edit" element={<StorageBlobUpdate />} />
      <Route path="delete" element={<StorageBlobDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StorageBlobRoutes;
