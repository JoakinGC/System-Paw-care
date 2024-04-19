import { IHistorial } from 'app/shared/model/historial.model';

export interface IMedicamento {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  historials?: IHistorial[] | null;
}

export const defaultValue: Readonly<IMedicamento> = {};
