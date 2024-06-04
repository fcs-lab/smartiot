import dayjs from 'dayjs';

export interface IManualEntry {
  id?: number;
  entryType?: string;
  value?: string;
  entryDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IManualEntry> = {};
