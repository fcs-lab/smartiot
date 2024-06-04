import dayjs from 'dayjs';

export interface INotification {
  id?: number;
  notificationType?: string;
  message?: string;
  sentDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<INotification> = {};
