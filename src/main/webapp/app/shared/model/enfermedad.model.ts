import { IRaza } from 'app/shared/model/raza.model';
import { IEspecie } from 'app/shared/model/especie.model';
import { ITerapia } from 'app/shared/model/terapia.model';
import { IFactores } from 'app/shared/model/factores.model';
import { IHistorial } from 'app/shared/model/historial.model';

export interface IEnfermedad {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  razas?: IRaza[] | null;
  especies?: IEspecie[] | null;
  terapias?: ITerapia[] | null;
  factores?: IFactores[] | null;
  historials?: IHistorial[] | null;
}

export const defaultValue: Readonly<IEnfermedad> = {};
