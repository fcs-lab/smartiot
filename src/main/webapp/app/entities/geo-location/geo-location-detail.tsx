import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './geo-location.reducer';

export const GeoLocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const geoLocationEntity = useAppSelector(state => state.geoLocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="geoLocationDetailsHeading">
          <Translate contentKey="smartiotApp.geoLocation.detail.title">GeoLocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{geoLocationEntity.id}</dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="smartiotApp.geoLocation.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{geoLocationEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="smartiotApp.geoLocation.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{geoLocationEntity.longitude}</dd>
          <dt>
            <span id="fullAddress">
              <Translate contentKey="smartiotApp.geoLocation.fullAddress">Full Address</Translate>
            </span>
          </dt>
          <dd>{geoLocationEntity.fullAddress}</dd>
        </dl>
        <Button tag={Link} to="/geo-location" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/geo-location/${geoLocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GeoLocationDetail;
