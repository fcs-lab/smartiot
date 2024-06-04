export interface IGeoLocation {
  id?: number;
  latitude?: number;
  longitude?: number;
  fullAddress?: string | null;
}

export const defaultValue: Readonly<IGeoLocation> = {};
