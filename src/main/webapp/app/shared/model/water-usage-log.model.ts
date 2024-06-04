import dayjs from 'dayjs';

export interface IWaterUsageLog {
  id?: number;
  usageDate?: dayjs.Dayjs;
  amountUsed?: number;
}

export const defaultValue: Readonly<IWaterUsageLog> = {};
