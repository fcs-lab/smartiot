import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChatUser } from 'app/shared/model/chat-user.model';
import { getEntities as getChatUsers } from 'app/entities/chat-user/chat-user.reducer';
import { IChatSession } from 'app/shared/model/chat-session.model';
import { getEntities as getChatSessions } from 'app/entities/chat-session/chat-session.reducer';
import { IChatMessage } from 'app/shared/model/chat-message.model';
import { getEntity, updateEntity, createEntity, reset } from './chat-message.reducer';

export const ChatMessageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const chatUsers = useAppSelector(state => state.chatUser.entities);
  const chatSessions = useAppSelector(state => state.chatSession.entities);
  const chatMessageEntity = useAppSelector(state => state.chatMessage.entity);
  const loading = useAppSelector(state => state.chatMessage.loading);
  const updating = useAppSelector(state => state.chatMessage.updating);
  const updateSuccess = useAppSelector(state => state.chatMessage.updateSuccess);

  const handleClose = () => {
    navigate('/chat-message' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getChatUsers({}));
    dispatch(getChatSessions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.messageTimestamp = convertDateTimeToServer(values.messageTimestamp);

    const entity = {
      ...chatMessageEntity,
      ...values,
      sender: chatUsers.find(it => it.id.toString() === values.sender?.toString()),
      chatSession: chatSessions.find(it => it.id.toString() === values.chatSession?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          messageTimestamp: displayDefaultDateTime(),
        }
      : {
          ...chatMessageEntity,
          messageTimestamp: convertDateTimeFromServer(chatMessageEntity.messageTimestamp),
          sender: chatMessageEntity?.sender?.id,
          chatSession: chatMessageEntity?.chatSession?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.chatMessage.home.createOrEditLabel" data-cy="ChatMessageCreateUpdateHeading">
            <Translate contentKey="smartiotApp.chatMessage.home.createOrEditLabel">Create or edit a ChatMessage</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="chat-message-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.chatMessage.messageId')}
                id="chat-message-messageId"
                name="messageId"
                data-cy="messageId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.chatMessage.messageContent')}
                id="chat-message-messageContent"
                name="messageContent"
                data-cy="messageContent"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.chatMessage.messageTimestamp')}
                id="chat-message-messageTimestamp"
                name="messageTimestamp"
                data-cy="messageTimestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="chat-message-sender"
                name="sender"
                data-cy="sender"
                label={translate('smartiotApp.chatMessage.sender')}
                type="select"
              >
                <option value="" key="0" />
                {chatUsers
                  ? chatUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="chat-message-chatSession"
                name="chatSession"
                data-cy="chatSession"
                label={translate('smartiotApp.chatMessage.chatSession')}
                type="select"
              >
                <option value="" key="0" />
                {chatSessions
                  ? chatSessions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chat-message" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ChatMessageUpdate;
