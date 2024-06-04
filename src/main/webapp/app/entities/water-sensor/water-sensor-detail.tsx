import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './water-sensor.reducer';

export const WaterSensorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const waterSensorEntity = useAppSelector(state => state.waterSensor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="waterSensorDetailsHeading">
          <Translate contentKey="smartiotApp.waterSensor.detail.title">WaterSensor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{waterSensorEntity.id}</dd>
          <dt>
            <span id="sensorId">
              <Translate contentKey="smartiotApp.waterSensor.sensorId">Sensor Id</Translate>
            </span>
          </dt>
          <dd>{waterSensorEntity.sensorId}</dd>
          <dt>
            <span id="sensorStatus">
              <Translate contentKey="smartiotApp.waterSensor.sensorStatus">Sensor Status</Translate>
            </span>
          </dt>
          <dd>{waterSensorEntity.sensorStatus}</dd>
        </dl>
        <Button tag={Link} to="/water-sensor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/water-sensor/${waterSensorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WaterSensorDetail;
