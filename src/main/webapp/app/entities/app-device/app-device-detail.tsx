import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './app-device.reducer';

export const AppDeviceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const appDeviceEntity = useAppSelector(state => state.appDevice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appDeviceDetailsHeading">
          <Translate contentKey="smartiotApp.appDevice.detail.title">AppDevice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{appDeviceEntity.id}</dd>
          <dt>
            <span id="deviceId">
              <Translate contentKey="smartiotApp.appDevice.deviceId">Device Id</Translate>
            </span>
          </dt>
          <dd>{appDeviceEntity.deviceId}</dd>
          <dt>
            <span id="deviceType">
              <Translate contentKey="smartiotApp.appDevice.deviceType">Device Type</Translate>
            </span>
          </dt>
          <dd>{appDeviceEntity.deviceType}</dd>
          <dt>
            <Translate contentKey="smartiotApp.appDevice.vehicle">Vehicle</Translate>
          </dt>
          <dd>{appDeviceEntity.vehicle ? appDeviceEntity.vehicle.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/app-device" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app-device/${appDeviceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppDeviceDetail;
