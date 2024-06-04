import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Measurement from './measurement';
import MeasurementDetail from './measurement-detail';
import MeasurementUpdate from './measurement-update';
import MeasurementDeleteDialog from './measurement-delete-dialog';

const MeasurementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Measurement />} />
    <Route path="new" element={<MeasurementUpdate />} />
    <Route path=":id">
      <Route index element={<MeasurementDetail />} />
      <Route path="edit" element={<MeasurementUpdate />} />
      <Route path="delete" element={<MeasurementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MeasurementRoutes;
