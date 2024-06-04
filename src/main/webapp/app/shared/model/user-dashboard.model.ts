export interface IUserDashboard {
  id?: number;
  dashboardName?: string;
  widgets?: string;
}

export const defaultValue: Readonly<IUserDashboard> = {};
