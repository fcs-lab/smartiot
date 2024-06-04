export interface IReport {
  id?: number;
  reportName?: string;
  reportData?: string;
}

export const defaultValue: Readonly<IReport> = {};
