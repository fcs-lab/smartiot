import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicleInfo } from 'app/shared/model/vehicle-info.model';
import { getEntities as getVehicleInfos } from 'app/entities/vehicle-info/vehicle-info.reducer';
import { IAppDevice } from 'app/shared/model/app-device.model';
import { DeviceType } from 'app/shared/model/enumerations/device-type.model';
import { getEntity, updateEntity, createEntity, reset } from './app-device.reducer';

export const AppDeviceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleInfos = useAppSelector(state => state.vehicleInfo.entities);
  const appDeviceEntity = useAppSelector(state => state.appDevice.entity);
  const loading = useAppSelector(state => state.appDevice.loading);
  const updating = useAppSelector(state => state.appDevice.updating);
  const updateSuccess = useAppSelector(state => state.appDevice.updateSuccess);
  const deviceTypeValues = Object.keys(DeviceType);

  const handleClose = () => {
    navigate('/app-device' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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

    const entity = {
      ...appDeviceEntity,
      ...values,
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
      ? {}
      : {
          deviceType: 'GPS',
          ...appDeviceEntity,
          vehicle: appDeviceEntity?.vehicle?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.appDevice.home.createOrEditLabel" data-cy="AppDeviceCreateUpdateHeading">
            <Translate contentKey="smartiotApp.appDevice.home.createOrEditLabel">Create or edit a AppDevice</Translate>
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
                  id="app-device-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.appDevice.deviceId')}
                id="app-device-deviceId"
                name="deviceId"
                data-cy="deviceId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.appDevice.deviceType')}
                id="app-device-deviceType"
                name="deviceType"
                data-cy="deviceType"
                type="select"
              >
                {deviceTypeValues.map(deviceType => (
                  <option value={deviceType} key={deviceType}>
                    {translate('smartiotApp.DeviceType.' + deviceType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="app-device-vehicle"
                name="vehicle"
                data-cy="vehicle"
                label={translate('smartiotApp.appDevice.vehicle')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/app-device" replace color="info">
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

export default AppDeviceUpdate;
