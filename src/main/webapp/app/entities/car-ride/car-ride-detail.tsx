import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './car-ride.reducer';

export const CarRideDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const carRideEntity = useAppSelector(state => state.carRide.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="carRideDetailsHeading">
          <Translate contentKey="smartiotApp.carRide.detail.title">CarRide</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{carRideEntity.id}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="smartiotApp.carRide.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{carRideEntity.startTime ? <TextFormat value={carRideEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="smartiotApp.carRide.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{carRideEntity.endTime ? <TextFormat value={carRideEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="origin">
              <Translate contentKey="smartiotApp.carRide.origin">Origin</Translate>
            </span>
          </dt>
          <dd>{carRideEntity.origin}</dd>
          <dt>
            <span id="destination">
              <Translate contentKey="smartiotApp.carRide.destination">Destination</Translate>
            </span>
          </dt>
          <dd>{carRideEntity.destination}</dd>
          <dt>
            <span id="availableSeats">
              <Translate contentKey="smartiotApp.carRide.availableSeats">Available Seats</Translate>
            </span>
          </dt>
          <dd>{carRideEntity.availableSeats}</dd>
          <dt>
            <Translate contentKey="smartiotApp.carRide.driver">Driver</Translate>
          </dt>
          <dd>{carRideEntity.driver ? carRideEntity.driver.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/car-ride" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/car-ride/${carRideEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CarRideDetail;
