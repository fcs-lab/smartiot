import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-report.reducer';

export const UserReportDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userReportEntity = useAppSelector(state => state.userReport.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userReportDetailsHeading">
          <Translate contentKey="smartiotApp.userReport.detail.title">UserReport</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.id}</dd>
          <dt>
            <span id="reportType">
              <Translate contentKey="smartiotApp.userReport.reportType">Report Type</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.reportType}</dd>
          <dt>
            <span id="generatedAt">
              <Translate contentKey="smartiotApp.userReport.generatedAt">Generated At</Translate>
            </span>
          </dt>
          <dd>
            {userReportEntity.generatedAt ? <TextFormat value={userReportEntity.generatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="reportContent">
              <Translate contentKey="smartiotApp.userReport.reportContent">Report Content</Translate>
            </span>
          </dt>
          <dd>{userReportEntity.reportContent}</dd>
        </dl>
        <Button tag={Link} to="/user-report" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-report/${userReportEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserReportDetail;
