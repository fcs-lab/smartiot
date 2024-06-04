import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IWaterUsageLog } from 'app/shared/model/water-usage-log.model';
import { getEntity, updateEntity, createEntity, reset } from './water-usage-log.reducer';

export const WaterUsageLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const waterUsageLogEntity = useAppSelector(state => state.waterUsageLog.entity);
  const loading = useAppSelector(state => state.waterUsageLog.loading);
  const updating = useAppSelector(state => state.waterUsageLog.updating);
  const updateSuccess = useAppSelector(state => state.waterUsageLog.updateSuccess);

  const handleClose = () => {
    navigate('/water-usage-log' + location.search);
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
    values.usageDate = convertDateTimeToServer(values.usageDate);
    if (values.amountUsed !== undefined && typeof values.amountUsed !== 'number') {
      values.amountUsed = Number(values.amountUsed);
    }

    const entity = {
      ...waterUsageLogEntity,
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
          usageDate: displayDefaultDateTime(),
        }
      : {
          ...waterUsageLogEntity,
          usageDate: convertDateTimeFromServer(waterUsageLogEntity.usageDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.waterUsageLog.home.createOrEditLabel" data-cy="WaterUsageLogCreateUpdateHeading">
            <Translate contentKey="smartiotApp.waterUsageLog.home.createOrEditLabel">Create or edit a WaterUsageLog</Translate>
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
                  id="water-usage-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.waterUsageLog.usageDate')}
                id="water-usage-log-usageDate"
                name="usageDate"
                data-cy="usageDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.waterUsageLog.amountUsed')}
                id="water-usage-log-amountUsed"
                name="amountUsed"
                data-cy="amountUsed"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/water-usage-log" replace color="info">
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

export default WaterUsageLogUpdate;
