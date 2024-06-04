import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pricing.reducer';

export const PricingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pricingEntity = useAppSelector(state => state.pricing.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pricingDetailsHeading">
          <Translate contentKey="smartiotApp.pricing.detail.title">Pricing</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pricingEntity.id}</dd>
          <dt>
            <span id="serviceType">
              <Translate contentKey="smartiotApp.pricing.serviceType">Service Type</Translate>
            </span>
          </dt>
          <dd>{pricingEntity.serviceType}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="smartiotApp.pricing.price">Price</Translate>
            </span>
          </dt>
          <dd>{pricingEntity.price}</dd>
        </dl>
        <Button tag={Link} to="/pricing" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pricing/${pricingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PricingDetail;
