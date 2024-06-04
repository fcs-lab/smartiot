import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-model.reducer';

export const VehicleModelDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleModelEntity = useAppSelector(state => state.vehicleModel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleModelDetailsHeading">
          <Translate contentKey="smartiotApp.vehicleModel.detail.title">VehicleModel</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleModelEntity.id}</dd>
          <dt>
            <span id="modelName">
              <Translate contentKey="smartiotApp.vehicleModel.modelName">Model Name</Translate>
            </span>
          </dt>
          <dd>{vehicleModelEntity.modelName}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-model" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-model/${vehicleModelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleModelDetail;
