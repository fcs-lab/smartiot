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

import { getEntities } from './device-telemetry.reducer';

export const DeviceTelemetry = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const deviceTelemetryList = useAppSelector(state => state.deviceTelemetry.entities);
  const loading = useAppSelector(state => state.deviceTelemetry.loading);
  const totalItems = useAppSelector(state => state.deviceTelemetry.totalItems);

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
      <h2 id="device-telemetry-heading" data-cy="DeviceTelemetryHeading">
        <Translate contentKey="smartiotApp.deviceTelemetry.home.title">Device Telemetries</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="smartiotApp.deviceTelemetry.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/device-telemetry/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="smartiotApp.deviceTelemetry.home.createLabel">Create new Device Telemetry</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {deviceTelemetryList && deviceTelemetryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="smartiotApp.deviceTelemetry.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('telemetryTimestamp')}>
                  <Translate contentKey="smartiotApp.deviceTelemetry.telemetryTimestamp">Telemetry Timestamp</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('telemetryTimestamp')} />
                </th>
                <th className="hand" onClick={sort('latitude')}>
                  <Translate contentKey="smartiotApp.deviceTelemetry.latitude">Latitude</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('latitude')} />
                </th>
                <th className="hand" onClick={sort('longitude')}>
                  <Translate contentKey="smartiotApp.deviceTelemetry.longitude">Longitude</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('longitude')} />
                </th>
                <th className="hand" onClick={sort('speed')}>
                  <Translate contentKey="smartiotApp.deviceTelemetry.speed">Speed</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('speed')} />
                </th>
                <th className="hand" onClick={sort('fuelLevel')}>
                  <Translate contentKey="smartiotApp.deviceTelemetry.fuelLevel">Fuel Level</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fuelLevel')} />
                </th>
                <th className="hand" onClick={sort('engineStatus')}>
                  <Translate contentKey="smartiotApp.deviceTelemetry.engineStatus">Engine Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('engineStatus')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {deviceTelemetryList.map((deviceTelemetry, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/device-telemetry/${deviceTelemetry.id}`} color="link" size="sm">
                      {deviceTelemetry.id}
                    </Button>
                  </td>
                  <td>
                    {deviceTelemetry.telemetryTimestamp ? (
                      <TextFormat type="date" value={deviceTelemetry.telemetryTimestamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{deviceTelemetry.latitude}</td>
                  <td>{deviceTelemetry.longitude}</td>
                  <td>{deviceTelemetry.speed}</td>
                  <td>{deviceTelemetry.fuelLevel}</td>
                  <td>{deviceTelemetry.engineStatus}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/device-telemetry/${deviceTelemetry.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/device-telemetry/${deviceTelemetry.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/device-telemetry/${deviceTelemetry.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="smartiotApp.deviceTelemetry.home.notFound">No Device Telemetries found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={deviceTelemetryList && deviceTelemetryList.length > 0 ? '' : 'd-none'}>
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

export default DeviceTelemetry;
