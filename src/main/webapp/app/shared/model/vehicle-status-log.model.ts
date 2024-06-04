import dayjs from 'dayjs';
import { VehicleStatus } from 'app/shared/model/enumerations/vehicle-status.model';

export interface IVehicleStatusLog {
  id?: number;
  statusChangeDate?: dayjs.Dayjs;
  newStatus?: keyof typeof VehicleStatus;
}

export const defaultValue: Readonly<IVehicleStatusLog> = {};
