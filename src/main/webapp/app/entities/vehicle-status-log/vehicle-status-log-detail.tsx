import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-status-log.reducer';

export const VehicleStatusLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleStatusLogEntity = useAppSelector(state => state.vehicleStatusLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleStatusLogDetailsHeading">
          <Translate contentKey="smartiotApp.vehicleStatusLog.detail.title">VehicleStatusLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleStatusLogEntity.id}</dd>
          <dt>
            <span id="statusChangeDate">
              <Translate contentKey="smartiotApp.vehicleStatusLog.statusChangeDate">Status Change Date</Translate>
            </span>
          </dt>
          <dd>
            {vehicleStatusLogEntity.statusChangeDate ? (
              <TextFormat value={vehicleStatusLogEntity.statusChangeDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="newStatus">
              <Translate contentKey="smartiotApp.vehicleStatusLog.newStatus">New Status</Translate>
            </span>
          </dt>
          <dd>{vehicleStatusLogEntity.newStatus}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-status-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-status-log/${vehicleStatusLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleStatusLogDetail;
