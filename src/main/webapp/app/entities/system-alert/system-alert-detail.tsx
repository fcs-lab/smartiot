import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './system-alert.reducer';

export const SystemAlertDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const systemAlertEntity = useAppSelector(state => state.systemAlert.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="systemAlertDetailsHeading">
          <Translate contentKey="smartiotApp.systemAlert.detail.title">SystemAlert</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{systemAlertEntity.id}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="smartiotApp.systemAlert.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {systemAlertEntity.createdAt ? <TextFormat value={systemAlertEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="smartiotApp.systemAlert.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {systemAlertEntity.updatedAt ? <TextFormat value={systemAlertEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="alertDescription">
              <Translate contentKey="smartiotApp.systemAlert.alertDescription">Alert Description</Translate>
            </span>
          </dt>
          <dd>{systemAlertEntity.alertDescription}</dd>
          <dt>
            <span id="alertType">
              <Translate contentKey="smartiotApp.systemAlert.alertType">Alert Type</Translate>
            </span>
          </dt>
          <dd>{systemAlertEntity.alertType}</dd>
          <dt>
            <Translate contentKey="smartiotApp.systemAlert.vehicle">Vehicle</Translate>
          </dt>
          <dd>{systemAlertEntity.vehicle ? systemAlertEntity.vehicle.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/system-alert" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/system-alert/${systemAlertEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SystemAlertDetail;
