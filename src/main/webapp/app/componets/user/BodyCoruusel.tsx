import React, { useEffect, useState } from 'react';
import CardProduct from './CardProduct';
import './user.css';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/producto/producto.reducer';



export const BodyCorousel = () => {
 
  const dispatch = useAppDispatch();

  const [selectedProducts, setSelectedProducts] = useState([]);

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const productoList = useAppSelector(state => state.producto.entities);
  const loading = useAppSelector(state => state.producto.loading);
  const totalItems = useAppSelector(state => state.producto.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const handleProductSelection = (productId) => {
    if (selectedProducts.includes(productId)) {
      // Si el producto ya está seleccionado, lo eliminamos del array
      setSelectedProducts(selectedProducts.filter(id => id !== productId));
    } else {
      // Si el producto no está seleccionado, lo añadimos al array
      setSelectedProducts([...selectedProducts, productId]);
    }
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

  productoList.map((e) => console.log(e));
  
    return (
        <>
          <h2 id="producto-heading" data-cy="ProductoHeading">
                <Translate contentKey="veterinarySystemApp.producto.home.title">Productos</Translate>
                <div className="d-flex justify-content-end">
                <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                    <FontAwesomeIcon icon="sync" spin={loading} />{' '}
                 <Translate contentKey="veterinarySystemApp.producto.home.refreshListLabel">Refresh List</Translate>
                    </Button>
                 </div>
             </h2>
            <div className="card-container">
            {productoList && productoList.length > 0 && productoList.map((producto, index) => (
              <div style={{ display: 'flex', flexDirection: 'column' }}>
                <CardProduct
                    key={index} // Asegúrate de proporcionar una key única para cada elemento en el mapa
                    cardText={producto.descripcion}
                    cardTitle={producto.nombre}
                    onClick={() => handleProductSelection(producto.id)}
                    isSelected={selectedProducts.includes(producto.id)}
                    link={producto.ruta}
                />
                <Button tag={Link} to={`/datelle-compra/${producto.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                        <Translate contentKey="entity.detail.see">Datil</Translate>
                        </span>
                </Button>
                
                </div>

            ))}
              
            </div>  
        </>       
    );
}

export default BodyCorousel;
