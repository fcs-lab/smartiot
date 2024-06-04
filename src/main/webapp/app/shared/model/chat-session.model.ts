import dayjs from 'dayjs';

export interface IChatSession {
  id?: number;
  sessionId?: string;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IChatSession> = {};
