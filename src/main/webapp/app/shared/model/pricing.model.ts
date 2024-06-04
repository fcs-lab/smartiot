export interface IPricing {
  id?: number;
  serviceType?: string;
  price?: number;
}

export const defaultValue: Readonly<IPricing> = {};
