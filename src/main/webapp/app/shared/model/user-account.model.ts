import dayjs from 'dayjs';
import { IApplicationUser } from 'app/shared/model/application-user.model';
import { CNHSituation } from 'app/shared/model/enumerations/cnh-situation.model';
import { RegisterSituation } from 'app/shared/model/enumerations/register-situation.model';

export interface IUserAccount {
  id?: number;
  accountName?: string;
  emailAddress?: string;
  admissionDate?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  isActive?: boolean;
  mobilePhone?: string | null;
  rpushFeedbackId?: number | null;
  execCommands?: boolean | null;
  isBlocked?: boolean | null;
  employerName?: string | null;
  pushConfiguration?: number | null;
  traveledDistance?: number | null;
  language?: string | null;
  blockedReason?: string | null;
  blockedById?: number | null;
  blockedAt?: dayjs.Dayjs | null;
  deletedReason?: string | null;
  deletedAt?: dayjs.Dayjs | null;
  deletedById?: number | null;
  lastModifiedBy?: string | null;
  registrationCode?: string | null;
  password?: string;
  passwordHint?: string | null;
  featureFlags?: string | null;
  zipCode?: string | null;
  publicPlace?: string | null;
  addressNumber?: string | null;
  streetName?: string | null;
  addressComplement?: string | null;
  cityName?: string | null;
  stateName?: string | null;
  cnhImage?: string | null;
  profileImage?: string | null;
  cnhExpirationDate?: dayjs.Dayjs | null;
  cnhStatus?: keyof typeof CNHSituation | null;
  registrationStatus?: keyof typeof RegisterSituation | null;
  analyzedBy?: string | null;
  analyzedAt?: dayjs.Dayjs | null;
  signatureImage?: string | null;
  residenceProofImage?: string | null;
  applicationUser?: IApplicationUser | null;
}

export const defaultValue: Readonly<IUserAccount> = {
  isActive: false,
  execCommands: false,
  isBlocked: false,
};
