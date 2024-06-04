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
import { IVehicleDamage } from 'app/shared/model/vehicle-damage.model';
import { DamageStatus } from 'app/shared/model/enumerations/damage-status.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-damage.reducer';

export const VehicleDamageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleInfos = useAppSelector(state => state.vehicleInfo.entities);
  const vehicleDamageEntity = useAppSelector(state => state.vehicleDamage.entity);
  const loading = useAppSelector(state => state.vehicleDamage.loading);
  const updating = useAppSelector(state => state.vehicleDamage.updating);
  const updateSuccess = useAppSelector(state => state.vehicleDamage.updateSuccess);
  const damageStatusValues = Object.keys(DamageStatus);

  const handleClose = () => {
    navigate('/vehicle-damage' + location.search);
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
    values.reportedAt = convertDateTimeToServer(values.reportedAt);

    const entity = {
      ...vehicleDamageEntity,
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
      ? {
          reportedAt: displayDefaultDateTime(),
        }
      : {
          damageStatus: 'REPORTED',
          ...vehicleDamageEntity,
          reportedAt: convertDateTimeFromServer(vehicleDamageEntity.reportedAt),
          vehicle: vehicleDamageEntity?.vehicle?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.vehicleDamage.home.createOrEditLabel" data-cy="VehicleDamageCreateUpdateHeading">
            <Translate contentKey="smartiotApp.vehicleDamage.home.createOrEditLabel">Create or edit a VehicleDamage</Translate>
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
                  id="vehicle-damage-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.vehicleDamage.damageDescription')}
                id="vehicle-damage-damageDescription"
                name="damageDescription"
                data-cy="damageDescription"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.vehicleDamage.reportedAt')}
                id="vehicle-damage-reportedAt"
                name="reportedAt"
                data-cy="reportedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.vehicleDamage.damageStatus')}
                id="vehicle-damage-damageStatus"
                name="damageStatus"
                data-cy="damageStatus"
                type="select"
              >
                {damageStatusValues.map(damageStatus => (
                  <option value={damageStatus} key={damageStatus}>
                    {translate('smartiotApp.DamageStatus.' + damageStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="vehicle-damage-vehicle"
                name="vehicle"
                data-cy="vehicle"
                label={translate('smartiotApp.vehicleDamage.vehicle')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-damage" replace color="info">
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

export default VehicleDamageUpdate;
