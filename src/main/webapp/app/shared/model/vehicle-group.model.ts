export interface IVehicleGroup {
  id?: number;
  groupName?: string;
  groupDescription?: string | null;
}

export const defaultValue: Readonly<IVehicleGroup> = {};
