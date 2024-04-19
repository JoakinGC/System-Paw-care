import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './cuidadora-hotel.reducer';

export const CuidadoraHotel = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const cuidadoraHotelList = useAppSelector(state => state.cuidadoraHotel.entities);
  const loading = useAppSelector(state => state.cuidadoraHotel.loading);
  const totalItems = useAppSelector(state => state.cuidadoraHotel.totalItems);

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

  return (
    <div>
      <h2 id="cuidadora-hotel-heading" data-cy="CuidadoraHotelHeading">
        <Translate contentKey="veterinarySystemApp.cuidadoraHotel.home.title">Cuidadora Hotels</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="veterinarySystemApp.cuidadoraHotel.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cuidadora-hotel/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="veterinarySystemApp.cuidadoraHotel.home.createLabel">Create new Cuidadora Hotel</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cuidadoraHotelList && cuidadoraHotelList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="veterinarySystemApp.cuidadoraHotel.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nombre')}>
                  <Translate contentKey="veterinarySystemApp.cuidadoraHotel.nombre">Nombre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nombre')} />
                </th>
                <th className="hand" onClick={sort('direccion')}>
                  <Translate contentKey="veterinarySystemApp.cuidadoraHotel.direccion">Direccion</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('direccion')} />
                </th>
                <th className="hand" onClick={sort('telefono')}>
                  <Translate contentKey="veterinarySystemApp.cuidadoraHotel.telefono">Telefono</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('telefono')} />
                </th>
                <th className="hand" onClick={sort('servicioOfrecido')}>
                  <Translate contentKey="veterinarySystemApp.cuidadoraHotel.servicioOfrecido">Servicio Ofrecido</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('servicioOfrecido')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cuidadoraHotelList.map((cuidadoraHotel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/cuidadora-hotel/${cuidadoraHotel.id}`} color="link" size="sm">
                      {cuidadoraHotel.id}
                    </Button>
                  </td>
                  <td>{cuidadoraHotel.nombre}</td>
                  <td>{cuidadoraHotel.direccion}</td>
                  <td>{cuidadoraHotel.telefono}</td>
                  <td>{cuidadoraHotel.servicioOfrecido}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/cuidadora-hotel/${cuidadoraHotel.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/cuidadora-hotel/${cuidadoraHotel.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/cuidadora-hotel/${cuidadoraHotel.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="veterinarySystemApp.cuidadoraHotel.home.notFound">No Cuidadora Hotels found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={cuidadoraHotelList && cuidadoraHotelList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default CuidadoraHotel;
