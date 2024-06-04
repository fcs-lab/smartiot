import dayjs from 'dayjs';

export interface IStorageAttachment {
  id?: number;
  attachmentName?: string;
  recordType?: string;
  recordId?: number;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  lastModifiedBy?: string | null;
  blobId?: number;
  deletedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IStorageAttachment> = {};
