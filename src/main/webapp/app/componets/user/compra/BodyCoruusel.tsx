import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import CardProduct from './CardProduct';
import './user.css';
import {  Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faShoppingCart } from '@fortawesome/free-solid-svg-icons';
import {  ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/producto/producto.reducer';

interface PropsBodyCorusel {
  isForm:Dispatch<SetStateAction<boolean>>;
  setProductos:Dispatch<any>;
}


function checkNonZeroValues(obj) {
  for (let key in obj) {
      if (obj.hasOwnProperty(key) && obj[key] !== 0) {
          return true; // Si encuentra un valor distinto de cero, devuelve true
      }
  }
  return false; // Si todos los valores son cero, devuelve false
}

export const BodyCorousel = ({isForm,setProductos}:PropsBodyCorusel) => {

  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search)
  );
  const productoList = useAppSelector(state => state.producto.entities);
  const loading = useAppSelector(state => state.producto.loading);

  // Estado para almacenar la cantidad seleccionada de cada producto
  const [productQuantities, setProductQuantities] = useState({});

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

  const handleSyncList = () => {
    sortEntities();
  };

  const handleQuantityChange = (productId, quantity) => {
    setProductQuantities((prevProductQuantities) => {
        const updatedProductQuantities = {
            ...prevProductQuantities,
            [productId]: quantity,
        };
        setProductos(updatedProductQuantities); 
        return updatedProductQuantities;
    });
};

  return (
    <>
      <h2 id="producto-heading" data-cy="ProductoHeading">
        <Translate contentKey="veterinarySystemApp.producto.home.title">Productos</Translate>
        <div className="d-flex justify-content-end">


          <FontAwesomeIcon icon={faShoppingCart} style={{marginRight:20}} onClick={() => (checkNonZeroValues(productQuantities))? isForm(true): isForm(false)}/>

          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="veterinarySystemApp.producto.home.refreshListLabel">Refresh List</Translate>
          </Button>
        </div>
      </h2>
      <div className="card-container">
        {productoList && productoList.length > 0 && productoList.map((producto, index) => (
          <div style={{ display: 'flex', flexDirection: 'column' }} key={index}>
            <CardProduct
              key={index}
              id={producto.id}
              cardText={producto.descripcion}
              cardTitle={producto.nombre}
              link={producto.ruta}
              quantity={productQuantities[producto.id] || 0}
              onQuantityChange={(quantity) => handleQuantityChange(producto.id, quantity)}
            />
          </div>
        ))}
      </div>
    </>
  );
};

export default BodyCorousel;
