import dayjs from 'dayjs';
import { IMedicamento } from 'app/shared/model/medicamento.model';
import { IEnfermedad } from 'app/shared/model/enfermedad.model';
import { IVeterinario } from 'app/shared/model/veterinario.model';
import { IMascota } from 'app/shared/model/mascota.model';

export interface IHistorial {
  id?: number;
  fechaConsulta?: dayjs.Dayjs;
  diagnostico?: string;
  medicamentos?: IMedicamento[] | null;
  enfermedads?: IEnfermedad[] | null;
  veterinario?: IVeterinario | null;
  mascota?: IMascota | null;
}

export const defaultValue: Readonly<IHistorial> = {};
