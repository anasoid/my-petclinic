export interface IOwner {
  id?: number;
  firstName?: string;
  lastName?: string | null;
  address?: string;
  city?: string;
  telephone?: string;
}

export const defaultValue: Readonly<IOwner> = {};
