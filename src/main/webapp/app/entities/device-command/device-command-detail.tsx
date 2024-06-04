import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './device-command.reducer';

export const DeviceCommandDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const deviceCommandEntity = useAppSelector(state => state.deviceCommand.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="deviceCommandDetailsHeading">
          <Translate contentKey="smartiotApp.deviceCommand.detail.title">DeviceCommand</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{deviceCommandEntity.id}</dd>
          <dt>
            <span id="commandType">
              <Translate contentKey="smartiotApp.deviceCommand.commandType">Command Type</Translate>
            </span>
          </dt>
          <dd>{deviceCommandEntity.commandType}</dd>
          <dt>
            <span id="sentAt">
              <Translate contentKey="smartiotApp.deviceCommand.sentAt">Sent At</Translate>
            </span>
          </dt>
          <dd>
            {deviceCommandEntity.sentAt ? <TextFormat value={deviceCommandEntity.sentAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="executedAt">
              <Translate contentKey="smartiotApp.deviceCommand.executedAt">Executed At</Translate>
            </span>
          </dt>
          <dd>
            {deviceCommandEntity.executedAt ? (
              <TextFormat value={deviceCommandEntity.executedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="commandStatus">
              <Translate contentKey="smartiotApp.deviceCommand.commandStatus">Command Status</Translate>
            </span>
          </dt>
          <dd>{deviceCommandEntity.commandStatus}</dd>
        </dl>
        <Button tag={Link} to="/device-command" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/device-command/${deviceCommandEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DeviceCommandDetail;
