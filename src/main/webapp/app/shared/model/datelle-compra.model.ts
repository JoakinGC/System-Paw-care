import { ICompra } from 'app/shared/model/compra.model';
import { IProducto } from 'app/shared/model/producto.model';

export interface IDatelleCompra {
  id?: number;
  cantidad?: number;
  precioUnitario?: number;
  totalProducto?: number;
  compra?: ICompra | null;
  producto?: IProducto | null;
}

export const defaultValue: Readonly<IDatelleCompra> = {};
