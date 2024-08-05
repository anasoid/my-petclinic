export interface IOwner {
  id?: number;
  firstName?: string;
  lastName?: string | null;
  address?: string;
  city?: string;
  telephone?: number;
  ffff?: string | null;
}

export const defaultValue: Readonly<IOwner> = {};
