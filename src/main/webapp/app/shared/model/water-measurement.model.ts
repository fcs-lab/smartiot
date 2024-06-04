import dayjs from 'dayjs';

export interface IWaterMeasurement {
  id?: number;
  measurementDate?: dayjs.Dayjs;
  waterLevel?: number;
  waterQuality?: string | null;
}

export const defaultValue: Readonly<IWaterMeasurement> = {};
