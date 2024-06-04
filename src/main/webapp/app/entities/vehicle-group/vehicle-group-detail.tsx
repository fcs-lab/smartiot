import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-group.reducer';

export const VehicleGroupDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleGroupEntity = useAppSelector(state => state.vehicleGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleGroupDetailsHeading">
          <Translate contentKey="smartiotApp.vehicleGroup.detail.title">VehicleGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleGroupEntity.id}</dd>
          <dt>
            <span id="groupName">
              <Translate contentKey="smartiotApp.vehicleGroup.groupName">Group Name</Translate>
            </span>
          </dt>
          <dd>{vehicleGroupEntity.groupName}</dd>
          <dt>
            <span id="groupDescription">
              <Translate contentKey="smartiotApp.vehicleGroup.groupDescription">Group Description</Translate>
            </span>
          </dt>
          <dd>{vehicleGroupEntity.groupDescription}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-group/${vehicleGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleGroupDetail;
