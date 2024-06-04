import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-dashboard.reducer';

export const UserDashboardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userDashboardEntity = useAppSelector(state => state.userDashboard.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userDashboardDetailsHeading">
          <Translate contentKey="smartiotApp.userDashboard.detail.title">UserDashboard</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userDashboardEntity.id}</dd>
          <dt>
            <span id="dashboardName">
              <Translate contentKey="smartiotApp.userDashboard.dashboardName">Dashboard Name</Translate>
            </span>
          </dt>
          <dd>{userDashboardEntity.dashboardName}</dd>
          <dt>
            <span id="widgets">
              <Translate contentKey="smartiotApp.userDashboard.widgets">Widgets</Translate>
            </span>
          </dt>
          <dd>{userDashboardEntity.widgets}</dd>
        </dl>
        <Button tag={Link} to="/user-dashboard" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-dashboard/${userDashboardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserDashboardDetail;
