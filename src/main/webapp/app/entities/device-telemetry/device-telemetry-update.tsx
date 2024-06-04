import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDeviceTelemetry } from 'app/shared/model/device-telemetry.model';
import { getEntity, updateEntity, createEntity, reset } from './device-telemetry.reducer';

export const DeviceTelemetryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const deviceTelemetryEntity = useAppSelector(state => state.deviceTelemetry.entity);
  const loading = useAppSelector(state => state.deviceTelemetry.loading);
  const updating = useAppSelector(state => state.deviceTelemetry.updating);
  const updateSuccess = useAppSelector(state => state.deviceTelemetry.updateSuccess);

  const handleClose = () => {
    navigate('/device-telemetry' + location.search);
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
    values.telemetryTimestamp = convertDateTimeToServer(values.telemetryTimestamp);
    if (values.latitude !== undefined && typeof values.latitude !== 'number') {
      values.latitude = Number(values.latitude);
    }
    if (values.longitude !== undefined && typeof values.longitude !== 'number') {
      values.longitude = Number(values.longitude);
    }
    if (values.speed !== undefined && typeof values.speed !== 'number') {
      values.speed = Number(values.speed);
    }
    if (values.fuelLevel !== undefined && typeof values.fuelLevel !== 'number') {
      values.fuelLevel = Number(values.fuelLevel);
    }

    const entity = {
      ...deviceTelemetryEntity,
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
          telemetryTimestamp: displayDefaultDateTime(),
        }
      : {
          ...deviceTelemetryEntity,
          telemetryTimestamp: convertDateTimeFromServer(deviceTelemetryEntity.telemetryTimestamp),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.deviceTelemetry.home.createOrEditLabel" data-cy="DeviceTelemetryCreateUpdateHeading">
            <Translate contentKey="smartiotApp.deviceTelemetry.home.createOrEditLabel">Create or edit a DeviceTelemetry</Translate>
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
                  id="device-telemetry-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.deviceTelemetry.telemetryTimestamp')}
                id="device-telemetry-telemetryTimestamp"
                name="telemetryTimestamp"
                data-cy="telemetryTimestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.deviceTelemetry.latitude')}
                id="device-telemetry-latitude"
                name="latitude"
                data-cy="latitude"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.deviceTelemetry.longitude')}
                id="device-telemetry-longitude"
                name="longitude"
                data-cy="longitude"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.deviceTelemetry.speed')}
                id="device-telemetry-speed"
                name="speed"
                data-cy="speed"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.deviceTelemetry.fuelLevel')}
                id="device-telemetry-fuelLevel"
                name="fuelLevel"
                data-cy="fuelLevel"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.deviceTelemetry.engineStatus')}
                id="device-telemetry-engineStatus"
                name="engineStatus"
                data-cy="engineStatus"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/device-telemetry" replace color="info">
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

export default DeviceTelemetryUpdate;
