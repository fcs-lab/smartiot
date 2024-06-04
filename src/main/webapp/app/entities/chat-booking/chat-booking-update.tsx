import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IChatSession } from 'app/shared/model/chat-session.model';
import { getEntities as getChatSessions } from 'app/entities/chat-session/chat-session.reducer';
import { IVehicleInfo } from 'app/shared/model/vehicle-info.model';
import { getEntities as getVehicleInfos } from 'app/entities/vehicle-info/vehicle-info.reducer';
import { IChatBooking } from 'app/shared/model/chat-booking.model';
import { getEntity, updateEntity, createEntity, reset } from './chat-booking.reducer';

export const ChatBookingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const chatSessions = useAppSelector(state => state.chatSession.entities);
  const vehicleInfos = useAppSelector(state => state.vehicleInfo.entities);
  const chatBookingEntity = useAppSelector(state => state.chatBooking.entity);
  const loading = useAppSelector(state => state.chatBooking.loading);
  const updating = useAppSelector(state => state.chatBooking.updating);
  const updateSuccess = useAppSelector(state => state.chatBooking.updateSuccess);

  const handleClose = () => {
    navigate('/chat-booking' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getChatSessions({}));
    dispatch(getVehicleInfos({}));
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
    values.bookingTimestamp = convertDateTimeToServer(values.bookingTimestamp);

    const entity = {
      ...chatBookingEntity,
      ...values,
      session: chatSessions.find(it => it.id.toString() === values.session?.toString()),
      vehicle: vehicleInfos.find(it => it.id.toString() === values.vehicle?.toString()),
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
          bookingTimestamp: displayDefaultDateTime(),
        }
      : {
          ...chatBookingEntity,
          bookingTimestamp: convertDateTimeFromServer(chatBookingEntity.bookingTimestamp),
          session: chatBookingEntity?.session?.id,
          vehicle: chatBookingEntity?.vehicle?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.chatBooking.home.createOrEditLabel" data-cy="ChatBookingCreateUpdateHeading">
            <Translate contentKey="smartiotApp.chatBooking.home.createOrEditLabel">Create or edit a ChatBooking</Translate>
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
                  id="chat-booking-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.chatBooking.bookingTimestamp')}
                id="chat-booking-bookingTimestamp"
                name="bookingTimestamp"
                data-cy="bookingTimestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="chat-booking-session"
                name="session"
                data-cy="session"
                label={translate('smartiotApp.chatBooking.session')}
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
              <ValidatedField
                id="chat-booking-vehicle"
                name="vehicle"
                data-cy="vehicle"
                label={translate('smartiotApp.chatBooking.vehicle')}
                type="select"
              >
                <option value="" key="0" />
                {vehicleInfos
                  ? vehicleInfos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chat-booking" replace color="info">
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

export default ChatBookingUpdate;
