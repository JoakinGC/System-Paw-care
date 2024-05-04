import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from '../../../entities/mascota/mascota.reducer';
import CardMascota from './CardMascota';


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
  
    const getAllEntities = () => {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        }),
      );
    };
  
    const sortEntities = () => {
      getAllEntities();
      const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
      if (pageLocation.search !== endURL) {
        navigate(`${pageLocation.pathname}${endURL}`);
      }
    };
  
    useEffect(() => {
      sortEntities();
    }, [paginationState.activePage, paginationState.order, paginationState.sort]);
  
    useEffect(() => {
      const params = new URLSearchParams(pageLocation.search);
      const page = params.get('page');
      const sort = params.get(SORT);
      if (page && sort) {
        const sortSplit = sort.split(',');
        setPaginationState({
          ...paginationState,
          activePage: +page,
          sort: sortSplit[0],
          order: sortSplit[1],
        });
      }
    }, [pageLocation.search]);
  
    const sort = p => () => {
      setPaginationState({
        ...paginationState,
        order: paginationState.order === ASC ? DESC : ASC,
        sort: p,
      });
    };
  
    const handlePagination = currentPage =>
      setPaginationState({
        ...paginationState,
        activePage: currentPage,
      });
  
    const handleSyncList = () => {
      sortEntities();
    };
  
    const getSortIconByFieldName = (fieldName: string) => {
      const sortFieldName = paginationState.sort;
      const order = paginationState.order;
      if (sortFieldName !== fieldName) {
        return faSort;
      } else {
        return order === ASC ? faSortUp : faSortDown;
      }
    };
    console.log(mascotaList);
    
    return (
        <div>
        {mascotaList && mascotaList.length > 0 ? (
          mascotaList.map((mascota,index) => (
            <CardMascota
              key={index} 
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
      </div>
      
    );
}


export default MascotaList;