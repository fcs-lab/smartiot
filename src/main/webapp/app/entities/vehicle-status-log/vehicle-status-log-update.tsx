import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicleStatusLog } from 'app/shared/model/vehicle-status-log.model';
import { VehicleStatus } from 'app/shared/model/enumerations/vehicle-status.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-status-log.reducer';

export const VehicleStatusLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleStatusLogEntity = useAppSelector(state => state.vehicleStatusLog.entity);
  const loading = useAppSelector(state => state.vehicleStatusLog.loading);
  const updating = useAppSelector(state => state.vehicleStatusLog.updating);
  const updateSuccess = useAppSelector(state => state.vehicleStatusLog.updateSuccess);
  const vehicleStatusValues = Object.keys(VehicleStatus);

  const handleClose = () => {
    navigate('/vehicle-status-log' + location.search);
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
    values.statusChangeDate = convertDateTimeToServer(values.statusChangeDate);

    const entity = {
      ...vehicleStatusLogEntity,
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
          statusChangeDate: displayDefaultDateTime(),
        }
      : {
          newStatus: 'AVAILABLE',
          ...vehicleStatusLogEntity,
          statusChangeDate: convertDateTimeFromServer(vehicleStatusLogEntity.statusChangeDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.vehicleStatusLog.home.createOrEditLabel" data-cy="VehicleStatusLogCreateUpdateHeading">
            <Translate contentKey="smartiotApp.vehicleStatusLog.home.createOrEditLabel">Create or edit a VehicleStatusLog</Translate>
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
                  id="vehicle-status-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.vehicleStatusLog.statusChangeDate')}
                id="vehicle-status-log-statusChangeDate"
                name="statusChangeDate"
                data-cy="statusChangeDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.vehicleStatusLog.newStatus')}
                id="vehicle-status-log-newStatus"
                name="newStatus"
                data-cy="newStatus"
                type="select"
              >
                {vehicleStatusValues.map(vehicleStatus => (
                  <option value={vehicleStatus} key={vehicleStatus}>
                    {translate('smartiotApp.VehicleStatus.' + vehicleStatus)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-status-log" replace color="info">
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

export default VehicleStatusLogUpdate;
