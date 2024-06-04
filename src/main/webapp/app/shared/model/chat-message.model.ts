import dayjs from 'dayjs';
import { IChatUser } from 'app/shared/model/chat-user.model';
import { IChatSession } from 'app/shared/model/chat-session.model';

export interface IChatMessage {
  id?: number;
  messageId?: string;
  messageContent?: string;
  messageTimestamp?: dayjs.Dayjs;
  sender?: IChatUser | null;
  chatSession?: IChatSession | null;
}

export const defaultValue: Readonly<IChatMessage> = {};
