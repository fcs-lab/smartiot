import dayjs from 'dayjs';
import { IVehicleInfo } from 'app/shared/model/vehicle-info.model';
import { DamageStatus } from 'app/shared/model/enumerations/damage-status.model';

export interface IVehicleDamage {
  id?: number;
  damageDescription?: string;
  reportedAt?: dayjs.Dayjs;
  damageStatus?: keyof typeof DamageStatus;
  vehicle?: IVehicleInfo | null;
}

export const defaultValue: Readonly<IVehicleDamage> = {};
