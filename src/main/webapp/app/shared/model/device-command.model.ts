import dayjs from 'dayjs';
import { CommandStatus } from 'app/shared/model/enumerations/command-status.model';

export interface IDeviceCommand {
  id?: number;
  commandType?: string;
  sentAt?: dayjs.Dayjs;
  executedAt?: dayjs.Dayjs | null;
  commandStatus?: keyof typeof CommandStatus;
}

export const defaultValue: Readonly<IDeviceCommand> = {};
