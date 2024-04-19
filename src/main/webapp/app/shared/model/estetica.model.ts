export interface IEstetica {
  id?: number;
  nombre?: string | null;
  direcion?: string | null;
  telefono?: string | null;
}

export const defaultValue: Readonly<IEstetica> = {};
