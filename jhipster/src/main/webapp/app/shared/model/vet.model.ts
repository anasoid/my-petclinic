import { ISpecialty } from 'app/shared/model/specialty.model';

export interface IVet {
  id?: number;
  firstName?: string;
  lastName?: string | null;
  specialties?: ISpecialty[] | null;
}

export const defaultValue: Readonly<IVet> = {};
