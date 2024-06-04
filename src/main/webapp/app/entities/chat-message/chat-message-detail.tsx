import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './chat-message.reducer';

export const ChatMessageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const chatMessageEntity = useAppSelector(state => state.chatMessage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="chatMessageDetailsHeading">
          <Translate contentKey="smartiotApp.chatMessage.detail.title">ChatMessage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{chatMessageEntity.id}</dd>
          <dt>
            <span id="messageId">
              <Translate contentKey="smartiotApp.chatMessage.messageId">Message Id</Translate>
            </span>
          </dt>
          <dd>{chatMessageEntity.messageId}</dd>
          <dt>
            <span id="messageContent">
              <Translate contentKey="smartiotApp.chatMessage.messageContent">Message Content</Translate>
            </span>
          </dt>
          <dd>{chatMessageEntity.messageContent}</dd>
          <dt>
            <span id="messageTimestamp">
              <Translate contentKey="smartiotApp.chatMessage.messageTimestamp">Message Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {chatMessageEntity.messageTimestamp ? (
              <TextFormat value={chatMessageEntity.messageTimestamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="smartiotApp.chatMessage.sender">Sender</Translate>
          </dt>
          <dd>{chatMessageEntity.sender ? chatMessageEntity.sender.id : ''}</dd>
          <dt>
            <Translate contentKey="smartiotApp.chatMessage.chatSession">Chat Session</Translate>
          </dt>
          <dd>{chatMessageEntity.chatSession ? chatMessageEntity.chatSession.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/chat-message" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/chat-message/${chatMessageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChatMessageDetail;
