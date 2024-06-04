import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './consumer.reducer';

export const ConsumerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const consumerEntity = useAppSelector(state => state.consumer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="consumerDetailsHeading">
          <Translate contentKey="smartiotApp.consumer.detail.title">Consumer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{consumerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="smartiotApp.consumer.name">Name</Translate>
            </span>
          </dt>
          <dd>{consumerEntity.name}</dd>
          <dt>
            <span id="street">
              <Translate contentKey="smartiotApp.consumer.street">Street</Translate>
            </span>
          </dt>
          <dd>{consumerEntity.street}</dd>
          <dt>
            <span id="neighborhood">
              <Translate contentKey="smartiotApp.consumer.neighborhood">Neighborhood</Translate>
            </span>
          </dt>
          <dd>{consumerEntity.neighborhood}</dd>
          <dt>
            <span id="propertyNumber">
              <Translate contentKey="smartiotApp.consumer.propertyNumber">Property Number</Translate>
            </span>
          </dt>
          <dd>{consumerEntity.propertyNumber}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="smartiotApp.consumer.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{consumerEntity.phone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="smartiotApp.consumer.email">Email</Translate>
            </span>
          </dt>
          <dd>{consumerEntity.email}</dd>
        </dl>
        <Button tag={Link} to="/consumer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/consumer/${consumerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ConsumerDetail;
