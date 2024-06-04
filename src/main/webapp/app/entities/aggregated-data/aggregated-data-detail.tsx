import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aggregated-data.reducer';

export const AggregatedDataDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const aggregatedDataEntity = useAppSelector(state => state.aggregatedData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aggregatedDataDetailsHeading">
          <Translate contentKey="smartiotApp.aggregatedData.detail.title">AggregatedData</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aggregatedDataEntity.id}</dd>
          <dt>
            <span id="dataType">
              <Translate contentKey="smartiotApp.aggregatedData.dataType">Data Type</Translate>
            </span>
          </dt>
          <dd>{aggregatedDataEntity.dataType}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="smartiotApp.aggregatedData.value">Value</Translate>
            </span>
          </dt>
          <dd>{aggregatedDataEntity.value}</dd>
          <dt>
            <span id="aggregationTime">
              <Translate contentKey="smartiotApp.aggregatedData.aggregationTime">Aggregation Time</Translate>
            </span>
          </dt>
          <dd>
            {aggregatedDataEntity.aggregationTime ? (
              <TextFormat value={aggregatedDataEntity.aggregationTime} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/aggregated-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aggregated-data/${aggregatedDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AggregatedDataDetail;
