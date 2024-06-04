import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ChatSession from './chat-session';
import ChatSessionDetail from './chat-session-detail';
import ChatSessionUpdate from './chat-session-update';
import ChatSessionDeleteDialog from './chat-session-delete-dialog';

const ChatSessionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ChatSession />} />
    <Route path="new" element={<ChatSessionUpdate />} />
    <Route path=":id">
      <Route index element={<ChatSessionDetail />} />
      <Route path="edit" element={<ChatSessionUpdate />} />
      <Route path="delete" element={<ChatSessionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChatSessionRoutes;
