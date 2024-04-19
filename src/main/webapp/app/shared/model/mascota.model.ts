import dayjs from 'dayjs';
import { ICita } from 'app/shared/model/cita.model';
import { IDueno } from 'app/shared/model/dueno.model';
import { IEspecie } from 'app/shared/model/especie.model';
import { IRaza } from 'app/shared/model/raza.model';

export interface IMascota {
  id?: number;
  nIdentificacionCarnet?: number;
  foto?: string;
  fechaNacimiento?: dayjs.Dayjs;
  citas?: ICita[] | null;
  dueno?: IDueno | null;
  especie?: IEspecie | null;
  raza?: IRaza | null;
}

export const defaultValue: Readonly<IMascota> = {};
