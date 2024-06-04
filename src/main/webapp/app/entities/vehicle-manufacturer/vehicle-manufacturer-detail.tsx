import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-manufacturer.reducer';

export const VehicleManufacturerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleManufacturerEntity = useAppSelector(state => state.vehicleManufacturer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleManufacturerDetailsHeading">
          <Translate contentKey="smartiotApp.vehicleManufacturer.detail.title">VehicleManufacturer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleManufacturerEntity.id}</dd>
          <dt>
            <span id="manufacturerName">
              <Translate contentKey="smartiotApp.vehicleManufacturer.manufacturerName">Manufacturer Name</Translate>
            </span>
          </dt>
          <dd>{vehicleManufacturerEntity.manufacturerName}</dd>
          <dt>
            <span id="manufacturerCountry">
              <Translate contentKey="smartiotApp.vehicleManufacturer.manufacturerCountry">Manufacturer Country</Translate>
            </span>
          </dt>
          <dd>{vehicleManufacturerEntity.manufacturerCountry}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-manufacturer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-manufacturer/${vehicleManufacturerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleManufacturerDetail;
