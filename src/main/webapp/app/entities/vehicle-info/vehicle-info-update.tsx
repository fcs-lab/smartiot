import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicleInfo } from 'app/shared/model/vehicle-info.model';
import { VehicleStatus } from 'app/shared/model/enumerations/vehicle-status.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-info.reducer';

export const VehicleInfoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleInfoEntity = useAppSelector(state => state.vehicleInfo.entity);
  const loading = useAppSelector(state => state.vehicleInfo.loading);
  const updating = useAppSelector(state => state.vehicleInfo.updating);
  const updateSuccess = useAppSelector(state => state.vehicleInfo.updateSuccess);
  const vehicleStatusValues = Object.keys(VehicleStatus);

  const handleClose = () => {
    navigate('/vehicle-info' + location.search);
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

    const entity = {
      ...vehicleInfoEntity,
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
      ? {}
      : {
          vehicleStatus: 'AVAILABLE',
          ...vehicleInfoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.vehicleInfo.home.createOrEditLabel" data-cy="VehicleInfoCreateUpdateHeading">
            <Translate contentKey="smartiotApp.vehicleInfo.home.createOrEditLabel">Create or edit a VehicleInfo</Translate>
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
                  id="vehicle-info-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.vehicleInfo.modelName')}
                id="vehicle-info-modelName"
                name="modelName"
                data-cy="modelName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.vehicleInfo.licensePlate')}
                id="vehicle-info-licensePlate"
                name="licensePlate"
                data-cy="licensePlate"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.vehicleInfo.vehicleStatus')}
                id="vehicle-info-vehicleStatus"
                name="vehicleStatus"
                data-cy="vehicleStatus"
                type="select"
              >
                {vehicleStatusValues.map(vehicleStatus => (
                  <option value={vehicleStatus} key={vehicleStatus}>
                    {translate('smartiotApp.VehicleStatus.' + vehicleStatus)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-info" replace color="info">
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

export default VehicleInfoUpdate;
