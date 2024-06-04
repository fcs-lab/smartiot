import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ChatBooking from './chat-booking';
import ChatBookingDetail from './chat-booking-detail';
import ChatBookingUpdate from './chat-booking-update';
import ChatBookingDeleteDialog from './chat-booking-delete-dialog';

const ChatBookingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ChatBooking />} />
    <Route path="new" element={<ChatBookingUpdate />} />
    <Route path=":id">
      <Route index element={<ChatBookingDetail />} />
      <Route path="edit" element={<ChatBookingUpdate />} />
      <Route path="delete" element={<ChatBookingDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChatBookingRoutes;
