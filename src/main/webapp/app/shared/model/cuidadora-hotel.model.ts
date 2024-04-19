export interface ICuidadoraHotel {
  id?: number;
  nombre?: string | null;
  direccion?: string | null;
  telefono?: string | null;
  servicioOfrecido?: string | null;
}

export const defaultValue: Readonly<ICuidadoraHotel> = {};
