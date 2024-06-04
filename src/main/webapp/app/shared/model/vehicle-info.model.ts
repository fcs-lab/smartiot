import { VehicleStatus } from 'app/shared/model/enumerations/vehicle-status.model';

export interface IVehicleInfo {
  id?: number;
  modelName?: string;
  licensePlate?: string;
  vehicleStatus?: keyof typeof VehicleStatus;
}

export const defaultValue: Readonly<IVehicleInfo> = {};
