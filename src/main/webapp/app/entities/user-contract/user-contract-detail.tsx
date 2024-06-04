import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-contract.reducer';

export const UserContractDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userContractEntity = useAppSelector(state => state.userContract.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userContractDetailsHeading">
          <Translate contentKey="smartiotApp.userContract.detail.title">UserContract</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userContractEntity.id}</dd>
          <dt>
            <span id="contractName">
              <Translate contentKey="smartiotApp.userContract.contractName">Contract Name</Translate>
            </span>
          </dt>
          <dd>{userContractEntity.contractName}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="smartiotApp.userContract.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {userContractEntity.startDate ? <TextFormat value={userContractEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="smartiotApp.userContract.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {userContractEntity.endDate ? <TextFormat value={userContractEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="smartiotApp.userContract.user">User</Translate>
          </dt>
          <dd>
            {userContractEntity.users
              ? userContractEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {userContractEntity.users && i === userContractEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-contract" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-contract/${userContractEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserContractDetail;
