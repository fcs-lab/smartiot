import dayjs from 'dayjs';
import { IEnrollment } from 'app/shared/model/enrollment.model';

export interface IMeasurement {
  id?: number;
  measurementType?: string;
  value?: string;
  measurementTime?: dayjs.Dayjs;
  enrollment?: IEnrollment | null;
}

export const defaultValue: Readonly<IMeasurement> = {};
