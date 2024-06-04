export interface ICostCenter {
  id?: number;
  centerName?: string;
  budgetAmount?: number;
}

export const defaultValue: Readonly<ICostCenter> = {};
