import dayjs from 'dayjs';
import { IHistorial } from 'app/shared/model/historial.model';

export interface ITratamiento {
  id?: number;
  fechaInicio?: dayjs.Dayjs;
  fechaFin?: dayjs.Dayjs;
  notas?: string | null;
  historial?: IHistorial | null;
}

export const defaultValue: Readonly<ITratamiento> = {};
