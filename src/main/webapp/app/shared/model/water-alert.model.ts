import dayjs from 'dayjs';

export interface IWaterAlert {
  id?: number;
  alertType?: string;
  alertDescription?: string;
  createdDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IWaterAlert> = {};
