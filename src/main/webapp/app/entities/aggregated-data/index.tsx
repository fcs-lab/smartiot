import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AggregatedData from './aggregated-data';
import AggregatedDataDetail from './aggregated-data-detail';
import AggregatedDataUpdate from './aggregated-data-update';
import AggregatedDataDeleteDialog from './aggregated-data-delete-dialog';

const AggregatedDataRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AggregatedData />} />
    <Route path="new" element={<AggregatedDataUpdate />} />
    <Route path=":id">
      <Route index element={<AggregatedDataDetail />} />
      <Route path="edit" element={<AggregatedDataUpdate />} />
      <Route path="delete" element={<AggregatedDataDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AggregatedDataRoutes;
