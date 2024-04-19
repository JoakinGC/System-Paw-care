import terapia from 'app/entities/terapia/terapia.reducer';
import factores from 'app/entities/factores/factores.reducer';
import enfermedad from 'app/entities/enfermedad/enfermedad.reducer';
import especie from 'app/entities/especie/especie.reducer';
import raza from 'app/entities/raza/raza.reducer';
import mascota from 'app/entities/mascota/mascota.reducer';
import dueno from 'app/entities/dueno/dueno.reducer';
import cita from 'app/entities/cita/cita.reducer';
import veterinario from 'app/entities/veterinario/veterinario.reducer';
import estudios from 'app/entities/estudios/estudios.reducer';
import estetica from 'app/entities/estetica/estetica.reducer';
import cuidadoraHotel from 'app/entities/cuidadora-hotel/cuidadora-hotel.reducer';
import historial from 'app/entities/historial/historial.reducer';
import tratamiento from 'app/entities/tratamiento/tratamiento.reducer';
import medicamento from 'app/entities/medicamento/medicamento.reducer';
import usuario from 'app/entities/usuario/usuario.reducer';
import compra from 'app/entities/compra/compra.reducer';
import datelleCompra from 'app/entities/datelle-compra/datelle-compra.reducer';
import producto from 'app/entities/producto/producto.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  terapia,
  factores,
  enfermedad,
  especie,
  raza,
  mascota,
  dueno,
  cita,
  veterinario,
  estudios,
  estetica,
  cuidadoraHotel,
  historial,
  tratamiento,
  medicamento,
  usuario,
  compra,
  datelleCompra,
  producto,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
