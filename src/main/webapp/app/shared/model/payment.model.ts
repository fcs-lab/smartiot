import dayjs from 'dayjs';

export interface IPayment {
  id?: number;
  amount?: number;
  paymentDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IPayment> = {};
