import dayjs from 'dayjs';

export interface IDeviceTelemetry {
  id?: number;
  telemetryTimestamp?: dayjs.Dayjs;
  latitude?: number;
  longitude?: number;
  speed?: number | null;
  fuelLevel?: number | null;
  engineStatus?: string | null;
}

export const defaultValue: Readonly<IDeviceTelemetry> = {};
