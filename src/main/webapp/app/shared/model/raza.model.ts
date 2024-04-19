import { IEnfermedad } from 'app/shared/model/enfermedad.model';

export interface IRaza {
  id?: number;
  nombre?: string | null;
  nombreCientifico?: string | null;
  enfermedads?: IEnfermedad[] | null;
}

export const defaultValue: Readonly<IRaza> = {};
