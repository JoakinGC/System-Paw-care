import { IEnfermedad } from 'app/shared/model/enfermedad.model';

export interface IFactores {
  id?: number;
  nombre?: string | null;
  tipo?: string | null;
  descripcion?: string | null;
  enfermedads?: IEnfermedad[] | null;
}

export const defaultValue: Readonly<IFactores> = {};
