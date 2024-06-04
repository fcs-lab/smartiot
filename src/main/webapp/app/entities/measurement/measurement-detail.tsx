import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './measurement.reducer';

export const MeasurementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const measurementEntity = useAppSelector(state => state.measurement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="measurementDetailsHeading">
          <Translate contentKey="smartiotApp.measurement.detail.title">Measurement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{measurementEntity.id}</dd>
          <dt>
            <span id="measurementType">
              <Translate contentKey="smartiotApp.measurement.measurementType">Measurement Type</Translate>
            </span>
          </dt>
          <dd>{measurementEntity.measurementType}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="smartiotApp.measurement.value">Value</Translate>
            </span>
          </dt>
          <dd>{measurementEntity.value}</dd>
          <dt>
            <span id="measurementTime">
              <Translate contentKey="smartiotApp.measurement.measurementTime">Measurement Time</Translate>
            </span>
          </dt>
          <dd>
            {measurementEntity.measurementTime ? (
              <TextFormat value={measurementEntity.measurementTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="smartiotApp.measurement.enrollment">Enrollment</Translate>
          </dt>
          <dd>{measurementEntity.enrollment ? measurementEntity.enrollment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/measurement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/measurement/${measurementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MeasurementDetail;
