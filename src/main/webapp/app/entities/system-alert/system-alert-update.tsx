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
import { ISystemAlert } from 'app/shared/model/system-alert.model';
import { getEntity, updateEntity, createEntity, reset } from './system-alert.reducer';

export const SystemAlertUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleInfos = useAppSelector(state => state.vehicleInfo.entities);
  const systemAlertEntity = useAppSelector(state => state.systemAlert.entity);
  const loading = useAppSelector(state => state.systemAlert.loading);
  const updating = useAppSelector(state => state.systemAlert.updating);
  const updateSuccess = useAppSelector(state => state.systemAlert.updateSuccess);

  const handleClose = () => {
    navigate('/system-alert' + location.search);
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...systemAlertEntity,
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
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...systemAlertEntity,
          createdAt: convertDateTimeFromServer(systemAlertEntity.createdAt),
          updatedAt: convertDateTimeFromServer(systemAlertEntity.updatedAt),
          vehicle: systemAlertEntity?.vehicle?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.systemAlert.home.createOrEditLabel" data-cy="SystemAlertCreateUpdateHeading">
            <Translate contentKey="smartiotApp.systemAlert.home.createOrEditLabel">Create or edit a SystemAlert</Translate>
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
                  id="system-alert-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.systemAlert.createdAt')}
                id="system-alert-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.systemAlert.updatedAt')}
                id="system-alert-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('smartiotApp.systemAlert.alertDescription')}
                id="system-alert-alertDescription"
                name="alertDescription"
                data-cy="alertDescription"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.systemAlert.alertType')}
                id="system-alert-alertType"
                name="alertType"
                data-cy="alertType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="system-alert-vehicle"
                name="vehicle"
                data-cy="vehicle"
                label={translate('smartiotApp.systemAlert.vehicle')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/system-alert" replace color="info">
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

export default SystemAlertUpdate;
