export interface IOwner {
  id?: number;
  firstName?: string;
  lastName?: string | null;
  address?: string;
  city?: string;
  telephone?: number;
}

export const defaultValue: Readonly<IOwner> = {};
