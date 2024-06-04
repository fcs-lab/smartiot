import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GeoLocation from './geo-location';
import GeoLocationDetail from './geo-location-detail';
import GeoLocationUpdate from './geo-location-update';
import GeoLocationDeleteDialog from './geo-location-delete-dialog';

const GeoLocationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GeoLocation />} />
    <Route path="new" element={<GeoLocationUpdate />} />
    <Route path=":id">
      <Route index element={<GeoLocationDetail />} />
      <Route path="edit" element={<GeoLocationUpdate />} />
      <Route path="delete" element={<GeoLocationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GeoLocationRoutes;
