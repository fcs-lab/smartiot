import { IUser } from 'app/shared/model/user.model';
import { IUserContract } from 'app/shared/model/user-contract.model';

export interface IApplicationUser {
  id?: number;
  userLogin?: string;
  firstName?: string;
  lastName?: string;
  emailAddress?: string;
  user?: IUser | null;
  contracts?: IUserContract[] | null;
}

export const defaultValue: Readonly<IApplicationUser> = {};
