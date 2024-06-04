import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './water-usage-log.reducer';

export const WaterUsageLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const waterUsageLogEntity = useAppSelector(state => state.waterUsageLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="waterUsageLogDetailsHeading">
          <Translate contentKey="smartiotApp.waterUsageLog.detail.title">WaterUsageLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{waterUsageLogEntity.id}</dd>
          <dt>
            <span id="usageDate">
              <Translate contentKey="smartiotApp.waterUsageLog.usageDate">Usage Date</Translate>
            </span>
          </dt>
          <dd>
            {waterUsageLogEntity.usageDate ? (
              <TextFormat value={waterUsageLogEntity.usageDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="amountUsed">
              <Translate contentKey="smartiotApp.waterUsageLog.amountUsed">Amount Used</Translate>
            </span>
          </dt>
          <dd>{waterUsageLogEntity.amountUsed}</dd>
        </dl>
        <Button tag={Link} to="/water-usage-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/water-usage-log/${waterUsageLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WaterUsageLogDetail;
