import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StorageAttachment from './storage-attachment';
import StorageAttachmentDetail from './storage-attachment-detail';
import StorageAttachmentUpdate from './storage-attachment-update';
import StorageAttachmentDeleteDialog from './storage-attachment-delete-dialog';

const StorageAttachmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StorageAttachment />} />
    <Route path="new" element={<StorageAttachmentUpdate />} />
    <Route path=":id">
      <Route index element={<StorageAttachmentDetail />} />
      <Route path="edit" element={<StorageAttachmentUpdate />} />
      <Route path="delete" element={<StorageAttachmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StorageAttachmentRoutes;
