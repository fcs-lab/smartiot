import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chat-session.reducer';

export const ChatSessionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chatSessionEntity = useAppSelector(state => state.chatSession.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chatSessionDetailsHeading">
          <Translate contentKey="smartiotApp.chatSession.detail.title">ChatSession</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chatSessionEntity.id}</dd>
          <dt>
            <span id="sessionId">
              <Translate contentKey="smartiotApp.chatSession.sessionId">Session Id</Translate>
            </span>
          </dt>
          <dd>{chatSessionEntity.sessionId}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="smartiotApp.chatSession.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>
            {chatSessionEntity.startTime ? <TextFormat value={chatSessionEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="smartiotApp.chatSession.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>
            {chatSessionEntity.endTime ? <TextFormat value={chatSessionEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/chat-session" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chat-session/${chatSessionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChatSessionDetail;
