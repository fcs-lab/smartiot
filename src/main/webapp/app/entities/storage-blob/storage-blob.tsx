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

import { getEntities } from './storage-blob.reducer';

export const StorageBlob = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const storageBlobList = useAppSelector(state => state.storageBlob.entities);
  const loading = useAppSelector(state => state.storageBlob.loading);
  const totalItems = useAppSelector(state => state.storageBlob.totalItems);

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
      <h2 id="storage-blob-heading" data-cy="StorageBlobHeading">
        <Translate contentKey="smartiotApp.storageBlob.home.title">Storage Blobs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="smartiotApp.storageBlob.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/storage-blob/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="smartiotApp.storageBlob.home.createLabel">Create new Storage Blob</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {storageBlobList && storageBlobList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="smartiotApp.storageBlob.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('fileName')}>
                  <Translate contentKey="smartiotApp.storageBlob.fileName">File Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileName')} />
                </th>
                <th className="hand" onClick={sort('contentType')}>
                  <Translate contentKey="smartiotApp.storageBlob.contentType">Content Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contentType')} />
                </th>
                <th className="hand" onClick={sort('byteSize')}>
                  <Translate contentKey="smartiotApp.storageBlob.byteSize">Byte Size</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('byteSize')} />
                </th>
                <th className="hand" onClick={sort('checksum')}>
                  <Translate contentKey="smartiotApp.storageBlob.checksum">Checksum</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checksum')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="smartiotApp.storageBlob.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="smartiotApp.storageBlob.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="smartiotApp.storageBlob.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('key')}>
                  <Translate contentKey="smartiotApp.storageBlob.key">Key</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('key')} />
                </th>
                <th className="hand" onClick={sort('metadata')}>
                  <Translate contentKey="smartiotApp.storageBlob.metadata">Metadata</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('metadata')} />
                </th>
                <th className="hand" onClick={sort('deletedAt')}>
                  <Translate contentKey="smartiotApp.storageBlob.deletedAt">Deleted At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedAt')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {storageBlobList.map((storageBlob, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/storage-blob/${storageBlob.id}`} color="link" size="sm">
                      {storageBlob.id}
                    </Button>
                  </td>
                  <td>{storageBlob.fileName}</td>
                  <td>{storageBlob.contentType}</td>
                  <td>{storageBlob.byteSize}</td>
                  <td>{storageBlob.checksum}</td>
                  <td>
                    {storageBlob.createdAt ? <TextFormat type="date" value={storageBlob.createdAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {storageBlob.updatedAt ? <TextFormat type="date" value={storageBlob.updatedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{storageBlob.lastModifiedBy}</td>
                  <td>{storageBlob.key}</td>
                  <td>{storageBlob.metadata}</td>
                  <td>
                    {storageBlob.deletedAt ? <TextFormat type="date" value={storageBlob.deletedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/storage-blob/${storageBlob.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/storage-blob/${storageBlob.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/storage-blob/${storageBlob.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="smartiotApp.storageBlob.home.notFound">No Storage Blobs found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={storageBlobList && storageBlobList.length > 0 ? '' : 'd-none'}>
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

export default StorageBlob;
