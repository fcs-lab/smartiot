import dayjs from 'dayjs';

export interface IEnrollment {
  id?: number;
  enrollmentType?: string;
  enrollmentDate?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IEnrollment> = {};
