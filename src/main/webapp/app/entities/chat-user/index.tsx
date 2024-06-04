import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ChatUser from './chat-user';
import ChatUserDetail from './chat-user-detail';
import ChatUserUpdate from './chat-user-update';
import ChatUserDeleteDialog from './chat-user-delete-dialog';

const ChatUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ChatUser />} />
    <Route path="new" element={<ChatUserUpdate />} />
    <Route path=":id">
      <Route index element={<ChatUserDetail />} />
      <Route path="edit" element={<ChatUserUpdate />} />
      <Route path="delete" element={<ChatUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChatUserRoutes;
