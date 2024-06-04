import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-sub-status.reducer';

export const VehicleSubStatusDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleSubStatusEntity = useAppSelector(state => state.vehicleSubStatus.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleSubStatusDetailsHeading">
          <Translate contentKey="smartiotApp.vehicleSubStatus.detail.title">VehicleSubStatus</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleSubStatusEntity.id}</dd>
          <dt>
            <span id="subStatusName">
              <Translate contentKey="smartiotApp.vehicleSubStatus.subStatusName">Sub Status Name</Translate>
            </span>
          </dt>
          <dd>{vehicleSubStatusEntity.subStatusName}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-sub-status" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-sub-status/${vehicleSubStatusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleSubStatusDetail;
