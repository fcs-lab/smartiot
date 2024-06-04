import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IWaterMeasurement } from 'app/shared/model/water-measurement.model';
import { getEntity, updateEntity, createEntity, reset } from './water-measurement.reducer';

export const WaterMeasurementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const waterMeasurementEntity = useAppSelector(state => state.waterMeasurement.entity);
  const loading = useAppSelector(state => state.waterMeasurement.loading);
  const updating = useAppSelector(state => state.waterMeasurement.updating);
  const updateSuccess = useAppSelector(state => state.waterMeasurement.updateSuccess);

  const handleClose = () => {
    navigate('/water-measurement' + location.search);
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
    values.measurementDate = convertDateTimeToServer(values.measurementDate);
    if (values.waterLevel !== undefined && typeof values.waterLevel !== 'number') {
      values.waterLevel = Number(values.waterLevel);
    }

    const entity = {
      ...waterMeasurementEntity,
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
          measurementDate: displayDefaultDateTime(),
        }
      : {
          ...waterMeasurementEntity,
          measurementDate: convertDateTimeFromServer(waterMeasurementEntity.measurementDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.waterMeasurement.home.createOrEditLabel" data-cy="WaterMeasurementCreateUpdateHeading">
            <Translate contentKey="smartiotApp.waterMeasurement.home.createOrEditLabel">Create or edit a WaterMeasurement</Translate>
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
                  id="water-measurement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.waterMeasurement.measurementDate')}
                id="water-measurement-measurementDate"
                name="measurementDate"
                data-cy="measurementDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.waterMeasurement.waterLevel')}
                id="water-measurement-waterLevel"
                name="waterLevel"
                data-cy="waterLevel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.waterMeasurement.waterQuality')}
                id="water-measurement-waterQuality"
                name="waterQuality"
                data-cy="waterQuality"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/water-measurement" replace color="info">
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

export default WaterMeasurementUpdate;
