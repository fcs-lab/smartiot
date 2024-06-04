export interface IConsumer {
  id?: number;
  name?: string;
  street?: string;
  neighborhood?: string;
  propertyNumber?: number;
  phone?: string;
  email?: string;
}

export const defaultValue: Readonly<IConsumer> = {};
