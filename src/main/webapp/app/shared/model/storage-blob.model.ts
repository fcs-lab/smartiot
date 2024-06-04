import dayjs from 'dayjs';

export interface IStorageBlob {
  id?: number;
  fileName?: string;
  contentType?: string | null;
  byteSize?: number;
  checksum?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  lastModifiedBy?: string | null;
  key?: string;
  metadata?: string | null;
  deletedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IStorageBlob> = {};
