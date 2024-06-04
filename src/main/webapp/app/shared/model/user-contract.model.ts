import dayjs from 'dayjs';
import { IApplicationUser } from 'app/shared/model/application-user.model';

export interface IUserContract {
  id?: number;
  contractName?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  users?: IApplicationUser[] | null;
}

export const defaultValue: Readonly<IUserContract> = {};
