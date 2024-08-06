import dayjs from 'dayjs';
import { IPet } from 'app/shared/model/pet.model';

export interface IVisit {
  id?: number;
  date?: dayjs.Dayjs;
  description?: string | null;
  pet?: IPet | null;
}

export const defaultValue: Readonly<IVisit> = {};
