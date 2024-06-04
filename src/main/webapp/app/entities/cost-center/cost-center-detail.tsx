import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cost-center.reducer';

export const CostCenterDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const costCenterEntity = useAppSelector(state => state.costCenter.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="costCenterDetailsHeading">
          <Translate contentKey="smartiotApp.costCenter.detail.title">CostCenter</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{costCenterEntity.id}</dd>
          <dt>
            <span id="centerName">
              <Translate contentKey="smartiotApp.costCenter.centerName">Center Name</Translate>
            </span>
          </dt>
          <dd>{costCenterEntity.centerName}</dd>
          <dt>
            <span id="budgetAmount">
              <Translate contentKey="smartiotApp.costCenter.budgetAmount">Budget Amount</Translate>
            </span>
          </dt>
          <dd>{costCenterEntity.budgetAmount}</dd>
        </dl>
        <Button tag={Link} to="/cost-center" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cost-center/${costCenterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CostCenterDetail;
