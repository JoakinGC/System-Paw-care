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
import CardMascota from './CardMascota';
import { getAccount } from 'app/shared/reducers/authentication';
import { getEntity as getDueno} from 'app/entities/dueno/dueno.reducer';
import { IMascota } from 'app/shared/model/mascota.model';
import axios from 'axios';
import ImageUploadForm from 'app/entities/image/ImageUploadForm';
import AddMascotaModal from './AddMascotaModal';
import { IDueno } from 'app/shared/model/dueno.model';


const MascotaList = () =>{
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
  
   
  useEffect(() =>{
    const geMascotasUserActual = async() =>{
        const user = await dispatch(getAccount());
        const { id } = (user.payload as any).data;
        const allUsarios = await dispatch(getUsuarios({}))
        const usuarioActual = (allUsarios.payload as any).data.filter((e,i) => 
        e.user.id==id);
  
        console.log(usuarioActual);
        console.log(usuarioActual[0].dueno.id);
        

        const duenoMn = ((await (dispatch as AppDispatch)(getDueno(usuarioActual[0].dueno.id))).payload as any).data;
        console.log("dueno" , duenoMn);
        setDuenoActual(duenoMn)
        
        if(mascotaList&&mascotaList.length>0){
            const mascotaUserActual = mascotaList.filter((e) =>{
                return (e.dueno.id===usuarioActual[0].dueno.id)
            })
            console.log(mascotaUserActual);
            setMascotasUser(mascotaUserActual)
        }
      }

      geMascotasUserActual()
  },[mascotaList])
    
  
    console.log(mascotaList);
    
    return (
        <div>
          <div style={{display:'flex', alignItems:'center',justifyContent:'center'}}>
            <h1>Tús Mascotas</h1>
          <Button style={{marginLeft:'3vw'}} color="primary" onClick={toggleModal}>Agregar Mascota</Button>
          </div>          
        {mascotasUser && mascotasUser.length > 0 ? (
          mascotasUser.map((mascota,index) => (
            <CardMascota
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
        <ImageUploadForm/>
      </div>
      
    );
}


export default MascotaList;












