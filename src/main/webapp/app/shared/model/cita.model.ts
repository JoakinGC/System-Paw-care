import dayjs from 'dayjs';
import { IEstetica } from 'app/shared/model/estetica.model';
import { ICuidadoraHotel } from 'app/shared/model/cuidadora-hotel.model';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import { IMascota } from 'app/shared/model/mascota.model';

export interface ICita {
  id?: number;
  hora?: dayjs.Dayjs | null;
  fecha?: dayjs.Dayjs | null;
  motivo?: string | null;
  estetica?: IEstetica | null;
  cuidadoraHotel?: ICuidadoraHotel | null;
  veterinario?: IVeterinario | null;
  mascotas?: IMascota[] | null;
}

export const defaultValue: Readonly<ICita> = {};
