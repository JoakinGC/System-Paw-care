import dayjs from 'dayjs';
import { IVeterinario } from 'app/shared/model/veterinario.model';

export interface IEstudios {
  id?: number;
  nombre?: string | null;
  fechaCursado?: dayjs.Dayjs;
  nombreInsituto?: string | null;
  veterinarios?: IVeterinario[] | null;
}

export const defaultValue: Readonly<IEstudios> = {};
