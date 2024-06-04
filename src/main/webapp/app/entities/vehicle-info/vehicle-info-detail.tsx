import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-info.reducer';

export const VehicleInfoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleInfoEntity = useAppSelector(state => state.vehicleInfo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleInfoDetailsHeading">
          <Translate contentKey="smartiotApp.vehicleInfo.detail.title">VehicleInfo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleInfoEntity.id}</dd>
          <dt>
            <span id="modelName">
              <Translate contentKey="smartiotApp.vehicleInfo.modelName">Model Name</Translate>
            </span>
          </dt>
          <dd>{vehicleInfoEntity.modelName}</dd>
          <dt>
            <span id="licensePlate">
              <Translate contentKey="smartiotApp.vehicleInfo.licensePlate">License Plate</Translate>
            </span>
          </dt>
          <dd>{vehicleInfoEntity.licensePlate}</dd>
          <dt>
            <span id="vehicleStatus">
              <Translate contentKey="smartiotApp.vehicleInfo.vehicleStatus">Vehicle Status</Translate>
            </span>
          </dt>
          <dd>{vehicleInfoEntity.vehicleStatus}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-info/${vehicleInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleInfoDetail;
