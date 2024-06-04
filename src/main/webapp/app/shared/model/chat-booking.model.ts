import dayjs from 'dayjs';
import { IChatSession } from 'app/shared/model/chat-session.model';
import { IVehicleInfo } from 'app/shared/model/vehicle-info.model';

export interface IChatBooking {
  id?: number;
  bookingTimestamp?: dayjs.Dayjs;
  session?: IChatSession | null;
  vehicle?: IVehicleInfo | null;
}

export const defaultValue: Readonly<IChatBooking> = {};
