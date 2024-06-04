import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './water-alert.reducer';

export const WaterAlertDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const waterAlertEntity = useAppSelector(state => state.waterAlert.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="waterAlertDetailsHeading">
          <Translate contentKey="smartiotApp.waterAlert.detail.title">WaterAlert</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{waterAlertEntity.id}</dd>
          <dt>
            <span id="alertType">
              <Translate contentKey="smartiotApp.waterAlert.alertType">Alert Type</Translate>
            </span>
          </dt>
          <dd>{waterAlertEntity.alertType}</dd>
          <dt>
            <span id="alertDescription">
              <Translate contentKey="smartiotApp.waterAlert.alertDescription">Alert Description</Translate>
            </span>
          </dt>
          <dd>{waterAlertEntity.alertDescription}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="smartiotApp.waterAlert.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {waterAlertEntity.createdDate ? <TextFormat value={waterAlertEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/water-alert" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/water-alert/${waterAlertEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WaterAlertDetail;
