import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDeviceCommand } from 'app/shared/model/device-command.model';
import { CommandStatus } from 'app/shared/model/enumerations/command-status.model';
import { getEntity, updateEntity, createEntity, reset } from './device-command.reducer';

export const DeviceCommandUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const deviceCommandEntity = useAppSelector(state => state.deviceCommand.entity);
  const loading = useAppSelector(state => state.deviceCommand.loading);
  const updating = useAppSelector(state => state.deviceCommand.updating);
  const updateSuccess = useAppSelector(state => state.deviceCommand.updateSuccess);
  const commandStatusValues = Object.keys(CommandStatus);

  const handleClose = () => {
    navigate('/device-command' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    values.sentAt = convertDateTimeToServer(values.sentAt);
    values.executedAt = convertDateTimeToServer(values.executedAt);

    const entity = {
      ...deviceCommandEntity,
      ...values,
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
          sentAt: displayDefaultDateTime(),
          executedAt: displayDefaultDateTime(),
        }
      : {
          commandStatus: 'PENDING',
          ...deviceCommandEntity,
          sentAt: convertDateTimeFromServer(deviceCommandEntity.sentAt),
          executedAt: convertDateTimeFromServer(deviceCommandEntity.executedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.deviceCommand.home.createOrEditLabel" data-cy="DeviceCommandCreateUpdateHeading">
            <Translate contentKey="smartiotApp.deviceCommand.home.createOrEditLabel">Create or edit a DeviceCommand</Translate>
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
                  id="device-command-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.deviceCommand.commandType')}
                id="device-command-commandType"
                name="commandType"
                data-cy="commandType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.deviceCommand.sentAt')}
                id="device-command-sentAt"
                name="sentAt"
                data-cy="sentAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.deviceCommand.executedAt')}
                id="device-command-executedAt"
                name="executedAt"
                data-cy="executedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('smartiotApp.deviceCommand.commandStatus')}
                id="device-command-commandStatus"
                name="commandStatus"
                data-cy="commandStatus"
                type="select"
              >
                {commandStatusValues.map(commandStatus => (
                  <option value={commandStatus} key={commandStatus}>
                    {translate('smartiotApp.CommandStatus.' + commandStatus)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/device-command" replace color="info">
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

export default DeviceCommandUpdate;
