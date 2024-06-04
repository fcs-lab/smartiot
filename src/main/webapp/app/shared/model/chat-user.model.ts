export interface IChatUser {
  id?: number;
  userId?: string;
  userName?: string;
}

export const defaultValue: Readonly<IChatUser> = {};
