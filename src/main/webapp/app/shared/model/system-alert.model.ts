import dayjs from 'dayjs';
import { IVehicleInfo } from 'app/shared/model/vehicle-info.model';

export interface ISystemAlert {
  id?: number;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  alertDescription?: string;
  alertType?: string;
  vehicle?: IVehicleInfo | null;
}

export const defaultValue: Readonly<ISystemAlert> = {};
