import dayjs from 'dayjs';
import { IApplicationUser } from 'app/shared/model/application-user.model';

export interface ICarRide {
  id?: number;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs;
  origin?: string;
  destination?: string;
  availableSeats?: number;
  driver?: IApplicationUser | null;
}

export const defaultValue: Readonly<ICarRide> = {};
