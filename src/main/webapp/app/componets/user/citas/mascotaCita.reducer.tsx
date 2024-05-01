import { EntityState } from '@reduxjs/toolkit';
import { ICita } from 'app/shared/model/cita.model';
import { IMascota } from 'app/shared/model/mascota.model'; // Importa el tipo de entidad

// Define el estado inicial utilizando EntityState con IMascota y el tipo de identificador (por ejemplo, number)
const initialState: EntityState<{ mascota: IMascota[]; citas: ICita[] }, number> = {
    entities: [], // Lista de entidades de mascota con citas
    ids: [], // Lista de IDs de las entidades
  };

// Define acciones para actualizar mascotas con citas
const UPDATE_MASCOTAS_WITH_CITAS = 'mascota/update_mascotas_with_citas';

// Creadores de acciones
export const updateMascotasWithCitas = (mascotasWithCitas: { mascota: IMascota; citas: ICita[] }[]) => ({
  type: UPDATE_MASCOTAS_WITH_CITAS,
  payload: mascotasWithCitas,
});

// Reducer
export default function mascotWithCitasReducer(state = initialState, action) {
  switch (action.type) {
    case UPDATE_MASCOTAS_WITH_CITAS:
      return {
        ...state,
        entities: action.payload,
      };
    default:
      return state;
  }
}
