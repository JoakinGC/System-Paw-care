import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { AppDispatch, useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUsuarios, getEntity as getUsuario} from 'app/entities/usuario/usuario.reducer';
import { createEntity, getEntities as getMascotas} from '../../../entities/mascota/mascota.reducer';
import { getAccount } from 'app/shared/reducers/authentication';
import { getEntity as getDueno} from 'app/entities/dueno/dueno.reducer';
import { IMascota } from 'app/shared/model/mascota.model';
import axios from 'axios';
import ImageUploadForm from 'app/entities/image/ImageUploadForm';
import { IDueno } from 'app/shared/model/dueno.model';
import FichaMascota from './FichaMascota';
import AddMascotaModal from './AddMascotaModal';


const MascotaListVeterinario = () =>{
    const dispatch = useAppDispatch();

    const pageLocation = useLocation();
    const navigate = useNavigate();
  
    const [paginationState, setPaginationState] = useState(
      overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
    );
  
    const mascotaList = useAppSelector(state => state.mascota.entities);
    const loading = useAppSelector(state => state.mascota.loading);
    const totalItems = useAppSelector(state => state.mascota.totalItems);
    const [mascotasUser,setMascotasUser] = useState<IMascota[]>();
    const [modalOpen, setModalOpen] = useState(false);
    const [duenoActual,setDuenoActual] =  useState<IDueno>({})

    const toggleModal = () => setModalOpen(!modalOpen);
  
    const getAllEntities = () => {
      dispatch(
        getMascotas({page:0,size:999,sort:`id,asc`}),
      );
    };
  
    useEffect(() =>{
      getAllEntities()
    },[])
  
 
    
  
    console.log(mascotaList);
    
    return (
        <div>
          <div style={{display:'flex', alignItems:'center',justifyContent:'center'}}>
            <h1>Todas las mascotas y sus citas</h1>
          <Button style={{marginLeft:'3vw'}} color="primary" onClick={toggleModal}>Agregar Mascota</Button>
          </div>          
        {mascotaList && mascotaList.length > 0 ? (
          mascotaList.map((mascota,index) => (
            <FichaMascota
              key={index} 
              id={mascota.id}
              dueno={mascota.dueno}
              especie={mascota.especie}
              fechaNacimiento={mascota.fechaNacimiento}
              nCarnet={mascota.nIdentificacionCarnet}
              raza={mascota.raza}
              urlImg={mascota.foto}
              citas={mascota.citas}
            />
          ))
        ) : !loading ? (
          <div className="alert alert-warning">
            <Translate contentKey="veterinarySystemApp.mascota.home.notFound">No Mascotas found</Translate>
          </div>
        ) : null}


          <AddMascotaModal dueno={duenoActual} isOpen={modalOpen} toggle={toggleModal} />
      </div>
      
    );
}


export default MascotaListVeterinario;












