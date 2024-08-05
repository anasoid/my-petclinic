export interface IPerson {
  id?: number;
  firstName?: string;
  lastName?: string | null;
}

export const defaultValue: Readonly<IPerson> = {};
