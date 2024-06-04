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
import { IVehicleService } from 'app/shared/model/vehicle-service.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-service.reducer';

export const VehicleServiceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleInfos = useAppSelector(state => state.vehicleInfo.entities);
  const vehicleServiceEntity = useAppSelector(state => state.vehicleService.entity);
  const loading = useAppSelector(state => state.vehicleService.loading);
  const updating = useAppSelector(state => state.vehicleService.updating);
  const updateSuccess = useAppSelector(state => state.vehicleService.updateSuccess);

  const handleClose = () => {
    navigate('/vehicle-service' + location.search);
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
      ...vehicleServiceEntity,
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
          ...vehicleServiceEntity,
          vehicle: vehicleServiceEntity?.vehicle?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.vehicleService.home.createOrEditLabel" data-cy="VehicleServiceCreateUpdateHeading">
            <Translate contentKey="smartiotApp.vehicleService.home.createOrEditLabel">Create or edit a VehicleService</Translate>
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
                  id="vehicle-service-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.vehicleService.serviceName')}
                id="vehicle-service-serviceName"
                name="serviceName"
                data-cy="serviceName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.vehicleService.serviceDate')}
                id="vehicle-service-serviceDate"
                name="serviceDate"
                data-cy="serviceDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.vehicleService.serviceDescription')}
                id="vehicle-service-serviceDescription"
                name="serviceDescription"
                data-cy="serviceDescription"
                type="text"
              />
              <ValidatedField
                id="vehicle-service-vehicle"
                name="vehicle"
                data-cy="vehicle"
                label={translate('smartiotApp.vehicleService.vehicle')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-service" replace color="info">
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

export default VehicleServiceUpdate;
