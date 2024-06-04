import dayjs from 'dayjs';

export interface IUserReport {
  id?: number;
  reportType?: string;
  generatedAt?: dayjs.Dayjs;
  reportContent?: string;
}

export const defaultValue: Readonly<IUserReport> = {};
