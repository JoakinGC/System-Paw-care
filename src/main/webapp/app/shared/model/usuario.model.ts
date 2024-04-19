import { IEstetica } from 'app/shared/model/estetica.model';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import { IDueno } from 'app/shared/model/dueno.model';

export interface IUsuario {
  id?: number;
  nombreUsuario?: string | null;
  rol?: string | null;
  estetica?: IEstetica | null;
  veterinario?: IVeterinario | null;
  dueno?: IDueno | null;
}

export const defaultValue: Readonly<IUsuario> = {};
