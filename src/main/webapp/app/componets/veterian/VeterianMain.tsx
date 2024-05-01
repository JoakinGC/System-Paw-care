import React, { useEffect, useState } from 'react';
import { AppDispatch } from 'app/config/store';
import { useDispatch } from 'react-redux';
import { getAccount } from 'app/shared/reducers/authentication';
import { getEntities as getUsuarios } from 'app/entities/usuario/usuario.reducer';
import { IUser } from 'app/shared/model/user.model';
import { useLocation, useNavigate } from 'react-router';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/cita/cita.reducer';
import { Link } from 'react-router-dom';

const VeterianMain = () =>{
    const [userActual, setUserActual] = useState<IUser>();
    const dispatch = useDispatch<AppDispatch>();
    const pageLocation = useLocation();
    const navigate = useNavigate();
    const [paginationState, setPaginationState] = useState(
        overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
      );
    
      const citaList = useAppSelector(state => state.cita.entities);
      const loading = useAppSelector(state => state.cita.loading);
      const totalItems = useAppSelector(state => state.cita.totalItems);
    
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
    useEffect(() =>{
        const fetchUser = async() =>{
            const user = await dispatch(getAccount());
            const { id } = (user.payload as any).data;
            const allUsarios = await dispatch(getUsuarios({}));
            const usuarioActual = await (allUsarios.payload as any).data.filter((e,i) => 
            e.user.id === id);  
            
            console.log(usuarioActual);
            setUserActual(usuarioActual);
        }
        fetchUser();
    }, []);

    console.log(userActual);

    return (
        <>
        <div className="container text-center">
            <h1>
                {userActual ? (
                    <> 
                        Bienvenido {userActual[0].nombreUsuario}
                    </>
                ) : (
                    <></>
                )}
            </h1>
        </div>
        <div>
      <h2 id="cita-heading" data-cy="CitaHeading">
        <Translate contentKey="veterinarySystemApp.cita.home.title">Citas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="veterinarySystemApp.cita.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cita/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="veterinarySystemApp.cita.home.createLabel">Create new Cita</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {citaList && citaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="veterinarySystemApp.cita.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('hora')}>
                  <Translate contentKey="veterinarySystemApp.cita.hora">Hora</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('hora')} />
                </th>
                <th className="hand" onClick={sort('fecha')}>
                  <Translate contentKey="veterinarySystemApp.cita.fecha">Fecha</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fecha')} />
                </th>
                <th className="hand" onClick={sort('motivo')}>
                  <Translate contentKey="veterinarySystemApp.cita.motivo">Motivo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('motivo')} />
                </th>
                <th>
                  <Translate contentKey="veterinarySystemApp.cita.estetica">Estetica</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="veterinarySystemApp.cita.cuidadoraHotel">Cuidadora Hotel</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="veterinarySystemApp.cita.veterinario">Veterinario</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {citaList.map((cita, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/cita/${cita.id}`} color="link" size="sm">
                      {cita.id}
                    </Button>
                  </td>
                  <td>{cita.hora ? <TextFormat type="time" value={cita.hora} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{cita.fecha ? <TextFormat type="date" value={cita.fecha} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{cita.motivo}</td>
                  <td>{cita.estetica ? <Link to={`/estetica/${cita.estetica.id}`}>{cita.estetica.id}</Link> : ''}</td>
                  <td>
                    {cita.cuidadoraHotel ? <Link to={`/cuidadora-hotel/${cita.cuidadoraHotel.id}`}>{cita.cuidadoraHotel.id}</Link> : ''}
                  </td>
                  <td>{cita.veterinario ? <Link to={`/veterinario/${cita.veterinario.id}`}>{cita.veterinario.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/cita/${cita.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/cita/${cita.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/cita/${cita.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="veterinarySystemApp.cita.home.notFound">No Citas found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={citaList && citaList.length > 0 ? '' : 'd-none'}>
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
    </>
    );
}

export default VeterianMain;
