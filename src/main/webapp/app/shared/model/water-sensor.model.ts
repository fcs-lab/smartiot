import { SensorStatus } from 'app/shared/model/enumerations/sensor-status.model';

export interface IWaterSensor {
  id?: number;
  sensorId?: string;
  sensorStatus?: keyof typeof SensorStatus;
}

export const defaultValue: Readonly<IWaterSensor> = {};
