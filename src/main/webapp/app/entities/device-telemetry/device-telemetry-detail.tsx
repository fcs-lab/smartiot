import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './device-telemetry.reducer';

export const DeviceTelemetryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const deviceTelemetryEntity = useAppSelector(state => state.deviceTelemetry.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="deviceTelemetryDetailsHeading">
          <Translate contentKey="smartiotApp.deviceTelemetry.detail.title">DeviceTelemetry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{deviceTelemetryEntity.id}</dd>
          <dt>
            <span id="telemetryTimestamp">
              <Translate contentKey="smartiotApp.deviceTelemetry.telemetryTimestamp">Telemetry Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {deviceTelemetryEntity.telemetryTimestamp ? (
              <TextFormat value={deviceTelemetryEntity.telemetryTimestamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="smartiotApp.deviceTelemetry.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{deviceTelemetryEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="smartiotApp.deviceTelemetry.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{deviceTelemetryEntity.longitude}</dd>
          <dt>
            <span id="speed">
              <Translate contentKey="smartiotApp.deviceTelemetry.speed">Speed</Translate>
            </span>
          </dt>
          <dd>{deviceTelemetryEntity.speed}</dd>
          <dt>
            <span id="fuelLevel">
              <Translate contentKey="smartiotApp.deviceTelemetry.fuelLevel">Fuel Level</Translate>
            </span>
          </dt>
          <dd>{deviceTelemetryEntity.fuelLevel}</dd>
          <dt>
            <span id="engineStatus">
              <Translate contentKey="smartiotApp.deviceTelemetry.engineStatus">Engine Status</Translate>
            </span>
          </dt>
          <dd>{deviceTelemetryEntity.engineStatus}</dd>
        </dl>
        <Button tag={Link} to="/device-telemetry" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/device-telemetry/${deviceTelemetryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DeviceTelemetryDetail;
