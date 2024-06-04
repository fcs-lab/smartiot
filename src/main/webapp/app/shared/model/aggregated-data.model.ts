import dayjs from 'dayjs';

export interface IAggregatedData {
  id?: number;
  dataType?: string;
  value?: string;
  aggregationTime?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IAggregatedData> = {};
