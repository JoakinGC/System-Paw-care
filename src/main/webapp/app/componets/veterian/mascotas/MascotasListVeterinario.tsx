import React, { useState, useEffect } from 'react';
import { Button, Collapse } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getMascotas } from '../../../entities/mascota/mascota.reducer';
import AddMascotaModal from './AddMascotaModal';
import { getEntities as getAllEspecies } from 'app/entities/especie/especie.reducer';
import { getEntities as getAllRazas } from 'app/entities/raza/raza.reducer';
import FichaMascota from './FichaMascota';
import './styleGrid.css';

const MascotaListVeterinario = () => {
  const dispatch = useAppDispatch();
  const [selectedRazas, setSelectedRazas] = useState([]);
  const mascotaList = useAppSelector(state => state.mascota.entities);
  const [modalOpen, setModalOpen] = useState(false);
  const especies = useAppSelector(state => state.especie.entities);
  const razas = useAppSelector(state => state.raza.entities);
  const [isOpen, setIsOpen] = useState(false);
  const [gatos, setGatos] = useState([]);
  const [perros, setPerros] = useState([]);
  const [animales, setAnimales] = useState([]);
  const [razaFiltrada, setRazaFiltrada] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 10;

  const toggleModal = () => setModalOpen(!modalOpen);

  const getAllEntities = () => {
    dispatch(getMascotas({ page: 0, size: 999, sort: 'id,asc' }));
    dispatch(getAllEspecies({ page: 0, size: 999, sort: 'id,asc' }));
    dispatch(getAllRazas({ page: 0, size: 999, sort: 'id,asc' }));
  };

  useEffect(() => {
    getAllEntities();
  }, []);

  useEffect(() => {
    const gatosFiltrados = mascotaList.filter(mascota => mascota.especie.id === especies[0]?.id);
    const perrosFiltrados = mascotaList.filter(mascota => mascota.especie.id === especies[1]?.id);
    const animalesFiltrados = mascotaList.filter(mascota => mascota.especie.id !== especies[0]?.id && mascota.especie.id !== especies[1]?.id);

    setGatos(gatosFiltrados);
    setPerros(perrosFiltrados);
    setAnimales(animalesFiltrados);
  }, [mascotaList, especies]);

  const filterMascotas = () => {
    if (selectedRazas && selectedRazas.length > 0 && selectedRazas.length <= 3) {
      const mascotaRazaFiltrados1 = mascotaList.filter((m) => m.raza.id == selectedRazas[0]);
      const mascotaRazaFiltrados2 = mascotaList.filter((m) => m.raza.id == selectedRazas[1]);
      const mascotaRazaFiltrados3 = mascotaList.filter((m) => m.raza.id == selectedRazas[2]);

      const mascotasYRazas = [
        ...mascotaRazaFiltrados1,
        ...mascotaRazaFiltrados2,
        ...mascotaRazaFiltrados3
      ];

      setRazaFiltrada(mascotasYRazas);
    }
  };

  const handleRazaChange = (e) => {
    const selectedOptions = Array.from(e.target.selectedOptions, (option:any) => option.value);
    setSelectedRazas(selectedOptions);
  };

  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  const renderMascotaGrid = (mascotas) => {
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = mascotas.slice(indexOfFirstItem, indexOfLastItem);

    return (
      <div className="row">
        {currentItems.map((mascota, index) => (
          <div className="col-md-4" key={index}>
            <FichaMascota
              id={mascota.id}
              dueno={mascota.dueno}
              especie={mascota.especie}
              fechaNacimiento={mascota.fechaNacimiento}
              nCarnet={mascota.nIdentificacionCarnet}
              raza={mascota.raza}
              urlImg={mascota.foto}
              citas={mascota.citas}
            />
          </div>
        ))}
      </div>
    );
  };

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <h1>Todas las mascotas y sus citas</h1>
        <Button style={{ marginLeft: '3vw' }} color="primary" onClick={toggleModal}>Agregar Mascota</Button>
      </div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        <Button color="primary" onClick={() => setIsOpen(!isOpen)}>Seleccionar Razas</Button>
        <Collapse isOpen={isOpen}>
          <select multiple onChange={handleRazaChange}>
            {razas.map(raza => (
              <option key={raza.id} value={raza.id}>{raza.nombre}</option>
            ))}
          </select>
        </Collapse>
        <Button color="primary" onClick={filterMascotas}>Filtrar</Button>
      </div>
      {renderMascotaGrid(mascotaList)}
      {gatos.length > 0 && renderMascotaGrid(gatos)}
      {perros.length > 0 && renderMascotaGrid(perros)}
      {animales.length > 0 && renderMascotaGrid(animales)}
      {razaFiltrada.length > 0 && renderMascotaGrid(razaFiltrada)}

      <div className="pagination">
        <Button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>Anterior</Button>
        <Button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === Math.ceil(mascotaList.length / itemsPerPage)}>Siguiente</Button>
      </div>
      
      <AddMascotaModal isOpen={modalOpen} toggle={toggleModal} />
    </div>
  );
};

export default MascotaListVeterinario;
