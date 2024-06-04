import { IVehicleInfo } from 'app/shared/model/vehicle-info.model';
import { DeviceType } from 'app/shared/model/enumerations/device-type.model';

export interface IAppDevice {
  id?: number;
  deviceId?: string;
  deviceType?: keyof typeof DeviceType;
  vehicle?: IVehicleInfo | null;
}

export const defaultValue: Readonly<IAppDevice> = {};
