import dayjs from 'dayjs';
import { IVehicleInfo } from 'app/shared/model/vehicle-info.model';

export interface IVehicleService {
  id?: number;
  serviceName?: string;
  serviceDate?: dayjs.Dayjs;
  serviceDescription?: string | null;
  vehicle?: IVehicleInfo | null;
}

export const defaultValue: Readonly<IVehicleService> = {};
