import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEnrollment } from 'app/shared/model/enrollment.model';
import { getEntities as getEnrollments } from 'app/entities/enrollment/enrollment.reducer';
import { IMeasurement } from 'app/shared/model/measurement.model';
import { getEntity, updateEntity, createEntity, reset } from './measurement.reducer';

export const MeasurementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const enrollments = useAppSelector(state => state.enrollment.entities);
  const measurementEntity = useAppSelector(state => state.measurement.entity);
  const loading = useAppSelector(state => state.measurement.loading);
  const updating = useAppSelector(state => state.measurement.updating);
  const updateSuccess = useAppSelector(state => state.measurement.updateSuccess);

  const handleClose = () => {
    navigate('/measurement' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEnrollments({}));
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
    values.measurementTime = convertDateTimeToServer(values.measurementTime);

    const entity = {
      ...measurementEntity,
      ...values,
      enrollment: enrollments.find(it => it.id.toString() === values.enrollment?.toString()),
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
          measurementTime: displayDefaultDateTime(),
        }
      : {
          ...measurementEntity,
          measurementTime: convertDateTimeFromServer(measurementEntity.measurementTime),
          enrollment: measurementEntity?.enrollment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.measurement.home.createOrEditLabel" data-cy="MeasurementCreateUpdateHeading">
            <Translate contentKey="smartiotApp.measurement.home.createOrEditLabel">Create or edit a Measurement</Translate>
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
                  id="measurement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.measurement.measurementType')}
                id="measurement-measurementType"
                name="measurementType"
                data-cy="measurementType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.measurement.value')}
                id="measurement-value"
                name="value"
                data-cy="value"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.measurement.measurementTime')}
                id="measurement-measurementTime"
                name="measurementTime"
                data-cy="measurementTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="measurement-enrollment"
                name="enrollment"
                data-cy="enrollment"
                label={translate('smartiotApp.measurement.enrollment')}
                type="select"
              >
                <option value="" key="0" />
                {enrollments
                  ? enrollments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/measurement" replace color="info">
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

export default MeasurementUpdate;
