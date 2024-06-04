import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-damage.reducer';

export const VehicleDamageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleDamageEntity = useAppSelector(state => state.vehicleDamage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleDamageDetailsHeading">
          <Translate contentKey="smartiotApp.vehicleDamage.detail.title">VehicleDamage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleDamageEntity.id}</dd>
          <dt>
            <span id="damageDescription">
              <Translate contentKey="smartiotApp.vehicleDamage.damageDescription">Damage Description</Translate>
            </span>
          </dt>
          <dd>{vehicleDamageEntity.damageDescription}</dd>
          <dt>
            <span id="reportedAt">
              <Translate contentKey="smartiotApp.vehicleDamage.reportedAt">Reported At</Translate>
            </span>
          </dt>
          <dd>
            {vehicleDamageEntity.reportedAt ? (
              <TextFormat value={vehicleDamageEntity.reportedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="damageStatus">
              <Translate contentKey="smartiotApp.vehicleDamage.damageStatus">Damage Status</Translate>
            </span>
          </dt>
          <dd>{vehicleDamageEntity.damageStatus}</dd>
          <dt>
            <Translate contentKey="smartiotApp.vehicleDamage.vehicle">Vehicle</Translate>
          </dt>
          <dd>{vehicleDamageEntity.vehicle ? vehicleDamageEntity.vehicle.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-damage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-damage/${vehicleDamageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleDamageDetail;
