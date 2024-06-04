import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './manual-entry.reducer';

export const ManualEntryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const manualEntryEntity = useAppSelector(state => state.manualEntry.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="manualEntryDetailsHeading">
          <Translate contentKey="smartiotApp.manualEntry.detail.title">ManualEntry</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{manualEntryEntity.id}</dd>
          <dt>
            <span id="entryType">
              <Translate contentKey="smartiotApp.manualEntry.entryType">Entry Type</Translate>
            </span>
          </dt>
          <dd>{manualEntryEntity.entryType}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="smartiotApp.manualEntry.value">Value</Translate>
            </span>
          </dt>
          <dd>{manualEntryEntity.value}</dd>
          <dt>
            <span id="entryDate">
              <Translate contentKey="smartiotApp.manualEntry.entryDate">Entry Date</Translate>
            </span>
          </dt>
          <dd>
            {manualEntryEntity.entryDate ? <TextFormat value={manualEntryEntity.entryDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/manual-entry" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/manual-entry/${manualEntryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ManualEntryDetail;
