import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chat-booking.reducer';

export const ChatBookingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chatBookingEntity = useAppSelector(state => state.chatBooking.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chatBookingDetailsHeading">
          <Translate contentKey="smartiotApp.chatBooking.detail.title">ChatBooking</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chatBookingEntity.id}</dd>
          <dt>
            <span id="bookingTimestamp">
              <Translate contentKey="smartiotApp.chatBooking.bookingTimestamp">Booking Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {chatBookingEntity.bookingTimestamp ? (
              <TextFormat value={chatBookingEntity.bookingTimestamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="smartiotApp.chatBooking.session">Session</Translate>
          </dt>
          <dd>{chatBookingEntity.session ? chatBookingEntity.session.id : ''}</dd>
          <dt>
            <Translate contentKey="smartiotApp.chatBooking.vehicle">Vehicle</Translate>
          </dt>
          <dd>{chatBookingEntity.vehicle ? chatBookingEntity.vehicle.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/chat-booking" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chat-booking/${chatBookingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChatBookingDetail;
