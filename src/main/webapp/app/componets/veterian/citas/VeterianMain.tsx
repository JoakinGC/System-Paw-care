import React, { useEffect, useState } from 'react';
import { getAccount } from 'app/shared/reducers/authentication';
import { getEntities as getUsuarios } from 'app/entities/usuario/usuario.reducer';
import { IUser } from 'app/shared/model/user.model';
import { Button } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/cita/cita.reducer';
import { ICita } from 'app/shared/model/cita.model';
import ModalAddCita from './ModalAddCita';
import dayjs from 'dayjs';
import CardCita from './CardCita';


const VeterianMain = () => {
  const dispatch = useAppDispatch();
  const [userActual, setUserActual] = useState<IUser | undefined>();
  const [showModal, setShowModal] = useState(false);

  const handleOpenModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  const [citasDiaDehoy, setCitasDiaDeHoy] = useState<ICita[]>([]);
  const [citasDiaDeMes, setCitasDiaDeMes] = useState<ICita[]>([]);
  const [citasDiaDeEstaSemana, setCitasDiaDeSmana] = useState<ICita[]>([]);
  const [todasLasCitasDelVeterinario, setCitasVeterinario] = useState<ICita[]>([]);

  const citaList = useAppSelector(state => state.cita.entities);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: 0,
        size: 999,
        sort: `id,asc`,
      })
    );
  };

  useEffect(() => {
    if (userActual && citaList.length > 0) {
      const citaFiltradas = citaList.filter(cita => 
        !cita.atendido && cita.estetica === null && cita.veterinario && cita.veterinario.id === userActual[0].id
      );
      setCitasVeterinario(citaFiltradas);
    }
  }, [citaList, userActual]);

  useEffect(() => {
    if (userActual && citaList.length > 0) {
      const citasOrdenadas = [...todasLasCitasDelVeterinario].sort((a, b) => {
        const dateA = new Date(a.fecha.toString());
        const dateB = new Date(b.fecha.toString());
        return dateA.getTime() - dateB.getTime();
      });

      const hoy = new Date().toDateString();

      const citasHoy = citasOrdenadas.filter((cita:any) => new Date(cita.fecha).toDateString() === hoy);
      setCitasDiaDeHoy(citasHoy);

      const mesActual = new Date().getMonth();
      const citasMesActual = citasOrdenadas.filter((cita:any) => new Date(cita.fecha).getMonth() === mesActual);
      setCitasDiaDeMes(citasMesActual);

      const hoySemana = new Date();
      const primerDiaSemana = new Date(hoySemana.setDate(hoySemana.getDate() - hoySemana.getDay()));
      const ultimoDiaSemana = new Date(hoySemana.setDate(hoySemana.getDate() - hoySemana.getDay() + 6));
      const citasSemanaActual = citasOrdenadas.filter((cita:any) => new Date(cita.fecha) >= primerDiaSemana && new Date(cita.fecha) <= ultimoDiaSemana);
      setCitasDiaDeSmana(citasSemanaActual);
    }
  }, [todasLasCitasDelVeterinario]);

  useEffect(() => {
    const fetchUser = async () => {
      const user = await dispatch(getAccount());
      const { id } = (user.payload as any).data;
      const allUsuarios = await dispatch(getUsuarios({}));
      const usuarioActual = (allUsuarios.payload as any).data.filter((e: any) => e.user.id === id);
      setUserActual(usuarioActual);
    };
    getAllEntities();
    fetchUser();
  }, [dispatch]);

  return (
    <>
      <div className="container text-center my-4">
        <h1>
          {userActual ? `Bienvenido ${userActual[0].nombreUsuario}` : ''}
        </h1>
        <Button color="primary" onClick={handleOpenModal} disabled={!userActual}>
          Agregar Cita
        </Button>
      </div>
      <div className="container">
        <h2>Citas de hoy</h2>
        <div className="row">
          {citasDiaDehoy.map((cita,i) => (
            <div key={cita.id} className="col-md-4 mb-4">
              <CardCita 
                id={cita.id}
                fecha={cita.fecha.toString()}
                hora={cita.hora.toString()}
                key={i}
                mascotas={cita.mascotas}
                motivo={cita.motivo}
              />
            </div>
          ))}
        </div>
        <h2>Citas de esta semana</h2>
        <div className="row">
          {citasDiaDeEstaSemana.map((cita,i) => (
            <div key={cita.id} className="col-md-4 mb-4">
              <CardCita 
                id={cita.id}
                fecha={cita.fecha.toString()}
                hora={cita.hora.toString()}
                key={i}
                mascotas={cita.mascotas}
                motivo={cita.motivo}
              />
            </div>
          ))}
        </div>
        <h2>Citas de este mes</h2>
        <div className="row">
          {citasDiaDeMes.map((cita,i) => (
            <div key={cita.id} className="col-md-4 mb-4">
              <CardCita 
                id={cita.id}
                fecha={cita.fecha.toString()}
                hora={cita.hora.toString()}
                key={i}
                mascotas={cita.mascotas}
                motivo={cita.motivo}
              />
            </div>
          ))}
        </div>
      </div>
      <ModalAddCita isOpen={showModal} toggle={handleCloseModal} veterinario={userActual && userActual[0]} citas={todasLasCitasDelVeterinario} />
    </>
  );
};

export default VeterianMain;
