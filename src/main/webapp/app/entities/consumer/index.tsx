import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Consumer from './consumer';
import ConsumerDetail from './consumer-detail';
import ConsumerUpdate from './consumer-update';
import ConsumerDeleteDialog from './consumer-delete-dialog';

const ConsumerRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Consumer />} />
    <Route path="new" element={<ConsumerUpdate />} />
    <Route path=":id">
      <Route index element={<ConsumerDetail />} />
      <Route path="edit" element={<ConsumerUpdate />} />
      <Route path="delete" element={<ConsumerDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ConsumerRoutes;
