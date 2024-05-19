import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Card, CardBody, CardSubtitle, CardText, CardTitle, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUsuarios} from 'app/entities/usuario/usuario.reducer';
import { getEntities as getAllHistoriales} from '../../../entities/historial/historial.reducer';
import { getAccount } from 'app/shared/reducers/authentication';
import { getEntities as getAllMascotas } from "app/entities/mascota/mascota.reducer";
import { getEntities as getAllVeterinarios, getEntity} from 'app/entities/veterinario/veterinario.reducer';
import { IHistorial } from 'app/shared/model/historial.model';
import { IMascota } from 'app/shared/model/mascota.model';
import { IVeterinario } from 'app/shared/model/veterinario.model';

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
  const mascotaList = useAppSelector(state => state.mascota.entities);
  const veterinarioList = useAppSelector(state => state.veterinario.entities);
  const [historyListUserActual,setHistoryListUserActual] = useState<any[]>([])

  const getAllEntities = () => {
    dispatch(
      getAllHistoriales({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );

    dispatch(getAllVeterinarios({page:0,size:999,sort:`id,asc`}))
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
        const mascotasUsuarioActual:IMascota[] = (todasLasMascotas.payload as any).data.filter(mascota => mascota.dueno?.id === idDuenoActual);
  
        console.log(mascotasUsuarioActual);
        console.log(historialList);
        

        const historialesMascotasUsuarioActual1 = historialList.filter(historial => {
            return mascotasUsuarioActual.some(mascota => mascota.id === historial.mascota?.id);
        });

        
        const historialesMascotasUsuarioActual2 = await Promise.all(historialesMascotasUsuarioActual1.map(async (historial: IHistorial) => {
          if (historial.mascota) {
              const n: IHistorial = {
                  diagnostico: historial.diagnostico,
                  enfermedads: historial.enfermedads,
                  fechaConsulta: historial.fechaConsulta,
                  id: historial.id,
                  mascota:  mascotasUsuarioActual.find((e: IMascota) => e.id === historial.mascota.id),
                  medicamentos: historial.medicamentos,
                  veterinario: await ((await dispatch(getEntity(historial.veterinario.id))).payload as any).data as IVeterinario
              };
              return n;
          }
          return historial;
      }));
      

        console.log(historialesMascotasUsuarioActual1);
        console.log(historialesMascotasUsuarioActual2);
        
        

        setHistoryListUserActual(historialesMascotasUsuarioActual2)
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
      <div>
        {historialList && historialList.length > 0 ? (
          <div className="d-flex flex-wrap justify-content-center">
          {historyListUserActual.map((historial, i) => (
            <Card key={`historial-${i}`} className="m-2" style={{ width: '18rem' }}>
              <CardBody>
                <CardSubtitle>
                  Fecha Consulta: {historial.fechaConsulta ? <TextFormat type="date" value={historial.fechaConsulta} format={APP_LOCAL_DATE_FORMAT} /> : null}
                </CardSubtitle>
                <CardText>Diagnóstico: {historial.diagnostico}</CardText>
                <CardText>
                  Veterinario: {historial.veterinario&&historial.veterinario.nombre} 
                </CardText>
                <CardText>
                  Mascota: {historial.mascota&&historial.mascota.nIdentificacionCarnet}
                </CardText>
                <Link to={`/historial/${historial.id}`} className="btn btn-info">
                  <FontAwesomeIcon icon="eye" /> <Translate contentKey="entity.action.view">View</Translate>
                </Link>
              </CardBody>
            </Card>
          ))}
        </div>
        
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
