import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-account.reducer';

export const UserAccount = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userAccountList = useAppSelector(state => state.userAccount.entities);
  const loading = useAppSelector(state => state.userAccount.loading);
  const totalItems = useAppSelector(state => state.userAccount.totalItems);

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
      <h2 id="user-account-heading" data-cy="UserAccountHeading">
        <Translate contentKey="smartiotApp.userAccount.home.title">User Accounts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="smartiotApp.userAccount.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-account/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="smartiotApp.userAccount.home.createLabel">Create new User Account</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userAccountList && userAccountList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="smartiotApp.userAccount.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('accountName')}>
                  <Translate contentKey="smartiotApp.userAccount.accountName">Account Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountName')} />
                </th>
                <th className="hand" onClick={sort('emailAddress')}>
                  <Translate contentKey="smartiotApp.userAccount.emailAddress">Email Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('emailAddress')} />
                </th>
                <th className="hand" onClick={sort('admissionDate')}>
                  <Translate contentKey="smartiotApp.userAccount.admissionDate">Admission Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('admissionDate')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="smartiotApp.userAccount.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="smartiotApp.userAccount.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="smartiotApp.userAccount.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('mobilePhone')}>
                  <Translate contentKey="smartiotApp.userAccount.mobilePhone">Mobile Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mobilePhone')} />
                </th>
                <th className="hand" onClick={sort('rpushFeedbackId')}>
                  <Translate contentKey="smartiotApp.userAccount.rpushFeedbackId">Rpush Feedback Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rpushFeedbackId')} />
                </th>
                <th className="hand" onClick={sort('execCommands')}>
                  <Translate contentKey="smartiotApp.userAccount.execCommands">Exec Commands</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('execCommands')} />
                </th>
                <th className="hand" onClick={sort('isBlocked')}>
                  <Translate contentKey="smartiotApp.userAccount.isBlocked">Is Blocked</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isBlocked')} />
                </th>
                <th className="hand" onClick={sort('employerName')}>
                  <Translate contentKey="smartiotApp.userAccount.employerName">Employer Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employerName')} />
                </th>
                <th className="hand" onClick={sort('pushConfiguration')}>
                  <Translate contentKey="smartiotApp.userAccount.pushConfiguration">Push Configuration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pushConfiguration')} />
                </th>
                <th className="hand" onClick={sort('traveledDistance')}>
                  <Translate contentKey="smartiotApp.userAccount.traveledDistance">Traveled Distance</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('traveledDistance')} />
                </th>
                <th className="hand" onClick={sort('language')}>
                  <Translate contentKey="smartiotApp.userAccount.language">Language</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('language')} />
                </th>
                <th className="hand" onClick={sort('blockedReason')}>
                  <Translate contentKey="smartiotApp.userAccount.blockedReason">Blocked Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('blockedReason')} />
                </th>
                <th className="hand" onClick={sort('blockedById')}>
                  <Translate contentKey="smartiotApp.userAccount.blockedById">Blocked By Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('blockedById')} />
                </th>
                <th className="hand" onClick={sort('blockedAt')}>
                  <Translate contentKey="smartiotApp.userAccount.blockedAt">Blocked At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('blockedAt')} />
                </th>
                <th className="hand" onClick={sort('deletedReason')}>
                  <Translate contentKey="smartiotApp.userAccount.deletedReason">Deleted Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedReason')} />
                </th>
                <th className="hand" onClick={sort('deletedAt')}>
                  <Translate contentKey="smartiotApp.userAccount.deletedAt">Deleted At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedAt')} />
                </th>
                <th className="hand" onClick={sort('deletedById')}>
                  <Translate contentKey="smartiotApp.userAccount.deletedById">Deleted By Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedById')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="smartiotApp.userAccount.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('registrationCode')}>
                  <Translate contentKey="smartiotApp.userAccount.registrationCode">Registration Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('registrationCode')} />
                </th>
                <th className="hand" onClick={sort('password')}>
                  <Translate contentKey="smartiotApp.userAccount.password">Password</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('password')} />
                </th>
                <th className="hand" onClick={sort('passwordHint')}>
                  <Translate contentKey="smartiotApp.userAccount.passwordHint">Password Hint</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('passwordHint')} />
                </th>
                <th className="hand" onClick={sort('featureFlags')}>
                  <Translate contentKey="smartiotApp.userAccount.featureFlags">Feature Flags</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('featureFlags')} />
                </th>
                <th className="hand" onClick={sort('zipCode')}>
                  <Translate contentKey="smartiotApp.userAccount.zipCode">Zip Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('zipCode')} />
                </th>
                <th className="hand" onClick={sort('publicPlace')}>
                  <Translate contentKey="smartiotApp.userAccount.publicPlace">Public Place</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('publicPlace')} />
                </th>
                <th className="hand" onClick={sort('addressNumber')}>
                  <Translate contentKey="smartiotApp.userAccount.addressNumber">Address Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addressNumber')} />
                </th>
                <th className="hand" onClick={sort('streetName')}>
                  <Translate contentKey="smartiotApp.userAccount.streetName">Street Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('streetName')} />
                </th>
                <th className="hand" onClick={sort('addressComplement')}>
                  <Translate contentKey="smartiotApp.userAccount.addressComplement">Address Complement</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addressComplement')} />
                </th>
                <th className="hand" onClick={sort('cityName')}>
                  <Translate contentKey="smartiotApp.userAccount.cityName">City Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cityName')} />
                </th>
                <th className="hand" onClick={sort('stateName')}>
                  <Translate contentKey="smartiotApp.userAccount.stateName">State Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stateName')} />
                </th>
                <th className="hand" onClick={sort('cnhImage')}>
                  <Translate contentKey="smartiotApp.userAccount.cnhImage">Cnh Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cnhImage')} />
                </th>
                <th className="hand" onClick={sort('profileImage')}>
                  <Translate contentKey="smartiotApp.userAccount.profileImage">Profile Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profileImage')} />
                </th>
                <th className="hand" onClick={sort('cnhExpirationDate')}>
                  <Translate contentKey="smartiotApp.userAccount.cnhExpirationDate">Cnh Expiration Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cnhExpirationDate')} />
                </th>
                <th className="hand" onClick={sort('cnhStatus')}>
                  <Translate contentKey="smartiotApp.userAccount.cnhStatus">Cnh Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cnhStatus')} />
                </th>
                <th className="hand" onClick={sort('registrationStatus')}>
                  <Translate contentKey="smartiotApp.userAccount.registrationStatus">Registration Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('registrationStatus')} />
                </th>
                <th className="hand" onClick={sort('analyzedBy')}>
                  <Translate contentKey="smartiotApp.userAccount.analyzedBy">Analyzed By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('analyzedBy')} />
                </th>
                <th className="hand" onClick={sort('analyzedAt')}>
                  <Translate contentKey="smartiotApp.userAccount.analyzedAt">Analyzed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('analyzedAt')} />
                </th>
                <th className="hand" onClick={sort('signatureImage')}>
                  <Translate contentKey="smartiotApp.userAccount.signatureImage">Signature Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('signatureImage')} />
                </th>
                <th className="hand" onClick={sort('residenceProofImage')}>
                  <Translate contentKey="smartiotApp.userAccount.residenceProofImage">Residence Proof Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('residenceProofImage')} />
                </th>
                <th>
                  <Translate contentKey="smartiotApp.userAccount.applicationUser">Application User</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userAccountList.map((userAccount, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-account/${userAccount.id}`} color="link" size="sm">
                      {userAccount.id}
                    </Button>
                  </td>
                  <td>{userAccount.accountName}</td>
                  <td>{userAccount.emailAddress}</td>
                  <td>
                    {userAccount.admissionDate ? (
                      <TextFormat type="date" value={userAccount.admissionDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {userAccount.createdAt ? <TextFormat type="date" value={userAccount.createdAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {userAccount.updatedAt ? <TextFormat type="date" value={userAccount.updatedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userAccount.isActive ? 'true' : 'false'}</td>
                  <td>{userAccount.mobilePhone}</td>
                  <td>{userAccount.rpushFeedbackId}</td>
                  <td>{userAccount.execCommands ? 'true' : 'false'}</td>
                  <td>{userAccount.isBlocked ? 'true' : 'false'}</td>
                  <td>{userAccount.employerName}</td>
                  <td>{userAccount.pushConfiguration}</td>
                  <td>{userAccount.traveledDistance}</td>
                  <td>{userAccount.language}</td>
                  <td>{userAccount.blockedReason}</td>
                  <td>{userAccount.blockedById}</td>
                  <td>
                    {userAccount.blockedAt ? <TextFormat type="date" value={userAccount.blockedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userAccount.deletedReason}</td>
                  <td>
                    {userAccount.deletedAt ? <TextFormat type="date" value={userAccount.deletedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userAccount.deletedById}</td>
                  <td>{userAccount.lastModifiedBy}</td>
                  <td>{userAccount.registrationCode}</td>
                  <td>{userAccount.password}</td>
                  <td>{userAccount.passwordHint}</td>
                  <td>{userAccount.featureFlags}</td>
                  <td>{userAccount.zipCode}</td>
                  <td>{userAccount.publicPlace}</td>
                  <td>{userAccount.addressNumber}</td>
                  <td>{userAccount.streetName}</td>
                  <td>{userAccount.addressComplement}</td>
                  <td>{userAccount.cityName}</td>
                  <td>{userAccount.stateName}</td>
                  <td>{userAccount.cnhImage}</td>
                  <td>{userAccount.profileImage}</td>
                  <td>
                    {userAccount.cnhExpirationDate ? (
                      <TextFormat type="date" value={userAccount.cnhExpirationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`smartiotApp.CNHSituation.${userAccount.cnhStatus}`} />
                  </td>
                  <td>
                    <Translate contentKey={`smartiotApp.RegisterSituation.${userAccount.registrationStatus}`} />
                  </td>
                  <td>{userAccount.analyzedBy}</td>
                  <td>
                    {userAccount.analyzedAt ? <TextFormat type="date" value={userAccount.analyzedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userAccount.signatureImage}</td>
                  <td>{userAccount.residenceProofImage}</td>
                  <td>
                    {userAccount.applicationUser ? (
                      <Link to={`/application-user/${userAccount.applicationUser.id}`}>{userAccount.applicationUser.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-account/${userAccount.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-account/${userAccount.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/user-account/${userAccount.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="smartiotApp.userAccount.home.notFound">No User Accounts found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userAccountList && userAccountList.length > 0 ? '' : 'd-none'}>
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

export default UserAccount;
