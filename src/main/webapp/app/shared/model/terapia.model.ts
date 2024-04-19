import { IEnfermedad } from 'app/shared/model/enfermedad.model';

export interface ITerapia {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  enfermedads?: IEnfermedad[] | null;
}

export const defaultValue: Readonly<ITerapia> = {};
