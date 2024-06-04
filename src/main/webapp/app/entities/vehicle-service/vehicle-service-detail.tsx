import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-service.reducer';

export const VehicleServiceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleServiceEntity = useAppSelector(state => state.vehicleService.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleServiceDetailsHeading">
          <Translate contentKey="smartiotApp.vehicleService.detail.title">VehicleService</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleServiceEntity.id}</dd>
          <dt>
            <span id="serviceName">
              <Translate contentKey="smartiotApp.vehicleService.serviceName">Service Name</Translate>
            </span>
          </dt>
          <dd>{vehicleServiceEntity.serviceName}</dd>
          <dt>
            <span id="serviceDate">
              <Translate contentKey="smartiotApp.vehicleService.serviceDate">Service Date</Translate>
            </span>
          </dt>
          <dd>
            {vehicleServiceEntity.serviceDate ? (
              <TextFormat value={vehicleServiceEntity.serviceDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="serviceDescription">
              <Translate contentKey="smartiotApp.vehicleService.serviceDescription">Service Description</Translate>
            </span>
          </dt>
          <dd>{vehicleServiceEntity.serviceDescription}</dd>
          <dt>
            <Translate contentKey="smartiotApp.vehicleService.vehicle">Vehicle</Translate>
          </dt>
          <dd>{vehicleServiceEntity.vehicle ? vehicleServiceEntity.vehicle.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-service" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-service/${vehicleServiceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleServiceDetail;
