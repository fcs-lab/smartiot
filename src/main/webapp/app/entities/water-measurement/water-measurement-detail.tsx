import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './water-measurement.reducer';

export const WaterMeasurementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const waterMeasurementEntity = useAppSelector(state => state.waterMeasurement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="waterMeasurementDetailsHeading">
          <Translate contentKey="smartiotApp.waterMeasurement.detail.title">WaterMeasurement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{waterMeasurementEntity.id}</dd>
          <dt>
            <span id="measurementDate">
              <Translate contentKey="smartiotApp.waterMeasurement.measurementDate">Measurement Date</Translate>
            </span>
          </dt>
          <dd>
            {waterMeasurementEntity.measurementDate ? (
              <TextFormat value={waterMeasurementEntity.measurementDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="waterLevel">
              <Translate contentKey="smartiotApp.waterMeasurement.waterLevel">Water Level</Translate>
            </span>
          </dt>
          <dd>{waterMeasurementEntity.waterLevel}</dd>
          <dt>
            <span id="waterQuality">
              <Translate contentKey="smartiotApp.waterMeasurement.waterQuality">Water Quality</Translate>
            </span>
          </dt>
          <dd>{waterMeasurementEntity.waterQuality}</dd>
        </dl>
        <Button tag={Link} to="/water-measurement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/water-measurement/${waterMeasurementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WaterMeasurementDetail;
