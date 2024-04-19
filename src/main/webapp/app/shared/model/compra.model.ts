import dayjs from 'dayjs';
import { IUsuario } from 'app/shared/model/usuario.model';

export interface ICompra {
  id?: number;
  fechaCompra?: dayjs.Dayjs;
  total?: number;
  usuario?: IUsuario | null;
}

export const defaultValue: Readonly<ICompra> = {};
