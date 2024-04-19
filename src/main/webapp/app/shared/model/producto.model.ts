export interface IProducto {
  id?: number;
  nombre?: number;
  descripcion?: string | null;
}

export const defaultValue: Readonly<IProducto> = {};
