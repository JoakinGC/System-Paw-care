import { IEstudios } from 'app/shared/model/estudios.model';

export interface IVeterinario {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  direccion?: string | null;
  telefono?: string | null;
  especilidad?: string | null;
  estudios?: IEstudios[] | null;
}

export const defaultValue: Readonly<IVeterinario> = {};
