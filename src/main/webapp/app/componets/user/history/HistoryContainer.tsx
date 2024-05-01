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
import { getEntities as getUsuarios, getEntity} from 'app/entities/usuario/usuario.reducer';
import { getEntities } from '../../../entities/historial/historial.reducer';
import { getAccount } from 'app/shared/reducers/authentication';
import { getEntities as getAllMascotas } from "app/entities/mascota/mascota.reducer";

export const HistoryContainer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const historialList = useAppSelector(state => state.historial.entities);
  const loading = useAppSelector(state => state.historial.loading);
  const totalItems = useAppSelector(state => state.historial.totalItems);
  const [historyListUserActual,setHistoryListUserActual] = useState<any[]>([])

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
    const citasFilterUserActual = async() =>{
        const user = await dispatch(getAccount());
        const { id } = (user.payload as any).data;
        const allUsuarios = await dispatch(getUsuarios({}));
        const usuarioActual = (allUsuarios.payload as any).data.find((e) => e.user.id === id);
        const idDuenoActual = usuarioActual?.dueno?.id;
  
        // Obtener todas las mascotas
        const todasLasMascotas = await dispatch(getAllMascotas({}));
  
        console.log(usuarioActual);
        console.log(idDuenoActual);
        
        // Filtrar las mascotas por el ID del dueño actual
        const mascotasUsuarioActual = (todasLasMascotas.payload as any).data.filter(mascota => mascota.dueno?.id === idDuenoActual);
  
        console.log(mascotasUsuarioActual);
        console.log(historialList);
        

        const historialesMascotasUsuarioActual = historialList.filter(historial => {
            return mascotasUsuarioActual.some(mascota => mascota.id === historial.mascota?.id);
        });

        console.log(historialesMascotasUsuarioActual);
        

        setHistoryListUserActual(historialesMascotasUsuarioActual)
    };
  
    citasFilterUserActual();
  }, [historialList]);
  
  
  return (
    <div>
      <h2 id="historial-heading" data-cy="HistorialHeading">
        <Translate contentKey="veterinarySystemApp.historial.home.title">Historials</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="veterinarySystemApp.historial.home.refreshListLabel">Refresh List</Translate>
          </Button>
        </div>
      </h2>
      <div className="table-responsive d-flex flex-row justify-content-center">
        {historialList && historialList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="veterinarySystemApp.historial.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('fechaConsulta')}>
                  <Translate contentKey="veterinarySystemApp.historial.fechaConsulta">Fecha Consulta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fechaConsulta')} />
                </th>
                <th className="hand" onClick={sort('diagnostico')}>
                  <Translate contentKey="veterinarySystemApp.historial.diagnostico">Diagnostico</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('diagnostico')} />
                </th>
                <th>
                  <Translate contentKey="veterinarySystemApp.historial.veterinario">Veterinario</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="veterinarySystemApp.historial.mascota">Mascota</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {historyListUserActual.map((historial, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/historial/${historial.id}`} color="link" size="sm">
                      {historial.id}
                    </Button>
                  </td>
                  <td>
                    {historial.fechaConsulta ? (
                      <TextFormat type="date" value={historial.fechaConsulta} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{historial.diagnostico}</td>
                  <td>
                    {historial.veterinario ? <Link to={`/veterinario/${historial.veterinario.id}`}>{historial.veterinario.id}</Link> : ''}
                  </td>
                  <td>{historial.mascota ? <Link to={`/mascota/${historial.mascota.id}`}>{historial.mascota.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/historial/${historial.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
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
              <Translate contentKey="veterinarySystemApp.historial.home.notFound">No Historials found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={historialList && historialList.length > 0 ? '' : 'd-none'}>
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

export default HistoryContainer;
