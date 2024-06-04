import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './application-user.reducer';

export const ApplicationUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const applicationUserEntity = useAppSelector(state => state.applicationUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="applicationUserDetailsHeading">
          <Translate contentKey="smartiotApp.applicationUser.detail.title">ApplicationUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.id}</dd>
          <dt>
            <span id="userLogin">
              <Translate contentKey="smartiotApp.applicationUser.userLogin">User Login</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.userLogin}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="smartiotApp.applicationUser.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="smartiotApp.applicationUser.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.lastName}</dd>
          <dt>
            <span id="emailAddress">
              <Translate contentKey="smartiotApp.applicationUser.emailAddress">Email Address</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.emailAddress}</dd>
          <dt>
            <Translate contentKey="smartiotApp.applicationUser.user">User</Translate>
          </dt>
          <dd>{applicationUserEntity.user ? applicationUserEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="smartiotApp.applicationUser.contracts">Contracts</Translate>
          </dt>
          <dd>
            {applicationUserEntity.contracts
              ? applicationUserEntity.contracts.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {applicationUserEntity.contracts && i === applicationUserEntity.contracts.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/application-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/application-user/${applicationUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ApplicationUserDetail;
