import dayjs from 'dayjs';
import { IConsumer } from 'app/shared/model/consumer.model';

export interface IAlert {
  id?: number;
  alertType?: string;
  description?: string;
  createdDate?: dayjs.Dayjs;
  consumer?: IConsumer | null;
}

export const defaultValue: Readonly<IAlert> = {};
