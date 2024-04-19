export interface IDueno {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  direccion?: string | null;
  telefono?: string | null;
}

export const defaultValue: Readonly<IDueno> = {};
