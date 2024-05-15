import React, { useState, useEffect } from 'react';
import { Button, Collapse  } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import {  MascotaSlice, getEntities as getMascotas} from '../../../entities/mascota/mascota.reducer';
import AddMascotaModal from './AddMascotaModal';
import SliderMascota from './SliderMascota';
import { getEntities as getAllEspecies} from 'app/entities/especie/especie.reducer';
import { getEntities as getAllRazas} from 'app/entities/raza/raza.reducer';
import { IMascota } from 'app/shared/model/mascota.model';


const MascotaListVeterinario = () =>{
    const dispatch = useAppDispatch();

    const [selectedRazas, setSelectedRazas] = useState([]);
    const mascotaList = useAppSelector(state => state.mascota.entities);
    const [modalOpen, setModalOpen] = useState(false);
    const especies = useAppSelector(state => state.especie.entities);
    const razas = useAppSelector(state => state.raza.entities);
    const [isOpen, setIsOpen] = useState(false);
    const [gatos,setGatos] = useState<IMascota[]|null>(null)
    const [perros,setPerros] = useState<IMascota[]|null>(null)
    const [animales,setAnimales] = useState<IMascota[]|null>(null)
    const [razaFiltrada,setRazaFiltrada]=useState<IMascota[]>([])

    const toggleModal = () => setModalOpen(!modalOpen);
  
    const getAllEntities = () => {
      dispatch(getMascotas({page:0,size:999,sort:`id,asc`}),);
      dispatch(getAllEspecies({page:0,size:999,sort:`id,asc`}))
      dispatch(getAllRazas({page:0,size:999,sort:`id,asc`}))
    };
  
    useEffect(() =>{
      getAllEntities()
    },[])

    useEffect(() => {
      const gatosFiltrados = mascotaList.filter(mascota => mascota.especie.id === especies[0]?.id);
      const perrosFiltrados = mascotaList.filter(mascota => mascota.especie.id === especies[1]?.id);
      const animalesFiltrados = mascotaList.filter(mascota => mascota.especie.id !== especies[0]?.id && mascota.especie.id !== especies[1]?.id);
    
      setGatos(gatosFiltrados);
      setPerros(perrosFiltrados);
      setAnimales(animalesFiltrados);
    }, [mascotaList, especies]);
    
  
    const filterMascotas = () => {
      if(selectedRazas && selectedRazas.length > 0&&selectedRazas.length<=3){
        const mascotaRazaFiltrados1 = mascotaList.filter((m,i) =>{
          return m.raza.id == selectedRazas[0];
        })
        const mascotaRazaFiltrados2 =mascotaList.filter((m,i) =>{
          return m.raza.id == selectedRazas[1];
        })

        const mascotaRazaFiltrados3 =mascotaList.filter((m,i) =>{
          return m.raza.id == selectedRazas[2];
        })

        const mascotasYRazas = [
          mascotaRazaFiltrados1,
          mascotaRazaFiltrados2,
          mascotaRazaFiltrados3
        ]

        console.log(mascotasYRazas);
        
        setRazaFiltrada(mascotasYRazas);
      }
    };
    
    const handleRazaChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
      const selectedOptions = Array.from(e.target.selectedOptions, (option: HTMLOptionElement) => option.value);
      setSelectedRazas(selectedOptions);
  };
  
  
    return (
        <div>
          <div style={{display:'flex', alignItems:'center',justifyContent:'center'}}>
            <h1>Todas las mascotas y sus citas</h1>
            
          <Button style={{marginLeft:'3vw'}} color="primary" onClick={toggleModal}>Agregar Mascota</Button>
          </div>    
          <div style={{display:'flex',alignItems:'center',justifyContent:'center'}}>
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
        <SliderMascota array={mascotaList}classname={'background1'}title={"Todas las mascotas"} key={0}/>
        
        {(gatos!=null&&gatos.length>0)?<SliderMascota array={gatos}classname={'background1'}title={"Todas las Gatos"} key={0}/>:<></>}
        {(perros!=null&&perros.length>0)?<SliderMascota array={perros}classname={'background1'}title={"Todas las perros"} key={0}/>:<></>}
        {(animales!=null&&animales.length>0)?<SliderMascota array={animales}classname={'background1'}title={"Todas los Animales"} key={0}/>:<></>}
        {(razaFiltrada&&razaFiltrada.length>0)?razaFiltrada.map((m,i)=>{
          return(
            <SliderMascota array={m}classname={'background1'}title={"Todas las razas filtradas"} key={i}/>
          );
        })
        
        :<></>}
        <AddMascotaModal  isOpen={modalOpen} toggle={toggleModal} />
      </div>
      
    );
}


export default MascotaListVeterinario;












