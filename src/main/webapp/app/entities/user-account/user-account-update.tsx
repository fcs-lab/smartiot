import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IApplicationUser } from 'app/shared/model/application-user.model';
import { getEntities as getApplicationUsers } from 'app/entities/application-user/application-user.reducer';
import { IUserAccount } from 'app/shared/model/user-account.model';
import { CNHSituation } from 'app/shared/model/enumerations/cnh-situation.model';
import { RegisterSituation } from 'app/shared/model/enumerations/register-situation.model';
import { getEntity, updateEntity, createEntity, reset } from './user-account.reducer';

export const UserAccountUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const applicationUsers = useAppSelector(state => state.applicationUser.entities);
  const userAccountEntity = useAppSelector(state => state.userAccount.entity);
  const loading = useAppSelector(state => state.userAccount.loading);
  const updating = useAppSelector(state => state.userAccount.updating);
  const updateSuccess = useAppSelector(state => state.userAccount.updateSuccess);
  const cNHSituationValues = Object.keys(CNHSituation);
  const registerSituationValues = Object.keys(RegisterSituation);

  const handleClose = () => {
    navigate('/user-account' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getApplicationUsers({}));
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
    if (values.rpushFeedbackId !== undefined && typeof values.rpushFeedbackId !== 'number') {
      values.rpushFeedbackId = Number(values.rpushFeedbackId);
    }
    if (values.pushConfiguration !== undefined && typeof values.pushConfiguration !== 'number') {
      values.pushConfiguration = Number(values.pushConfiguration);
    }
    if (values.traveledDistance !== undefined && typeof values.traveledDistance !== 'number') {
      values.traveledDistance = Number(values.traveledDistance);
    }
    if (values.blockedById !== undefined && typeof values.blockedById !== 'number') {
      values.blockedById = Number(values.blockedById);
    }
    values.blockedAt = convertDateTimeToServer(values.blockedAt);
    values.deletedAt = convertDateTimeToServer(values.deletedAt);
    if (values.deletedById !== undefined && typeof values.deletedById !== 'number') {
      values.deletedById = Number(values.deletedById);
    }
    values.analyzedAt = convertDateTimeToServer(values.analyzedAt);

    const entity = {
      ...userAccountEntity,
      ...values,
      applicationUser: applicationUsers.find(it => it.id.toString() === values.applicationUser?.toString()),
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
          blockedAt: displayDefaultDateTime(),
          deletedAt: displayDefaultDateTime(),
          analyzedAt: displayDefaultDateTime(),
        }
      : {
          cnhStatus: 'VALID',
          registrationStatus: 'PRE_REGISTRATION',
          ...userAccountEntity,
          createdAt: convertDateTimeFromServer(userAccountEntity.createdAt),
          updatedAt: convertDateTimeFromServer(userAccountEntity.updatedAt),
          blockedAt: convertDateTimeFromServer(userAccountEntity.blockedAt),
          deletedAt: convertDateTimeFromServer(userAccountEntity.deletedAt),
          analyzedAt: convertDateTimeFromServer(userAccountEntity.analyzedAt),
          applicationUser: userAccountEntity?.applicationUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartiotApp.userAccount.home.createOrEditLabel" data-cy="UserAccountCreateUpdateHeading">
            <Translate contentKey="smartiotApp.userAccount.home.createOrEditLabel">Create or edit a UserAccount</Translate>
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
                  id="user-account-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartiotApp.userAccount.accountName')}
                id="user-account-accountName"
                name="accountName"
                data-cy="accountName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.emailAddress')}
                id="user-account-emailAddress"
                name="emailAddress"
                data-cy="emailAddress"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  pattern: {
                    value: /^[^@]+@[^@]+\.[^@]+$/,
                    message: translate('entity.validation.pattern', { pattern: '^[^@]+@[^@]+\\.[^@]+$' }),
                  },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.admissionDate')}
                id="user-account-admissionDate"
                name="admissionDate"
                data-cy="admissionDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.createdAt')}
                id="user-account-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.updatedAt')}
                id="user-account-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.isActive')}
                id="user-account-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.mobilePhone')}
                id="user-account-mobilePhone"
                name="mobilePhone"
                data-cy="mobilePhone"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.rpushFeedbackId')}
                id="user-account-rpushFeedbackId"
                name="rpushFeedbackId"
                data-cy="rpushFeedbackId"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.execCommands')}
                id="user-account-execCommands"
                name="execCommands"
                data-cy="execCommands"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.isBlocked')}
                id="user-account-isBlocked"
                name="isBlocked"
                data-cy="isBlocked"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.employerName')}
                id="user-account-employerName"
                name="employerName"
                data-cy="employerName"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.pushConfiguration')}
                id="user-account-pushConfiguration"
                name="pushConfiguration"
                data-cy="pushConfiguration"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.traveledDistance')}
                id="user-account-traveledDistance"
                name="traveledDistance"
                data-cy="traveledDistance"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.language')}
                id="user-account-language"
                name="language"
                data-cy="language"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.blockedReason')}
                id="user-account-blockedReason"
                name="blockedReason"
                data-cy="blockedReason"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.blockedById')}
                id="user-account-blockedById"
                name="blockedById"
                data-cy="blockedById"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.blockedAt')}
                id="user-account-blockedAt"
                name="blockedAt"
                data-cy="blockedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.deletedReason')}
                id="user-account-deletedReason"
                name="deletedReason"
                data-cy="deletedReason"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.deletedAt')}
                id="user-account-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.deletedById')}
                id="user-account-deletedById"
                name="deletedById"
                data-cy="deletedById"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.lastModifiedBy')}
                id="user-account-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.registrationCode')}
                id="user-account-registrationCode"
                name="registrationCode"
                data-cy="registrationCode"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.password')}
                id="user-account-password"
                name="password"
                data-cy="password"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 8, message: translate('entity.validation.minlength', { min: 8 }) },
                }}
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.passwordHint')}
                id="user-account-passwordHint"
                name="passwordHint"
                data-cy="passwordHint"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.featureFlags')}
                id="user-account-featureFlags"
                name="featureFlags"
                data-cy="featureFlags"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.zipCode')}
                id="user-account-zipCode"
                name="zipCode"
                data-cy="zipCode"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.publicPlace')}
                id="user-account-publicPlace"
                name="publicPlace"
                data-cy="publicPlace"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.addressNumber')}
                id="user-account-addressNumber"
                name="addressNumber"
                data-cy="addressNumber"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.streetName')}
                id="user-account-streetName"
                name="streetName"
                data-cy="streetName"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.addressComplement')}
                id="user-account-addressComplement"
                name="addressComplement"
                data-cy="addressComplement"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.cityName')}
                id="user-account-cityName"
                name="cityName"
                data-cy="cityName"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.stateName')}
                id="user-account-stateName"
                name="stateName"
                data-cy="stateName"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.cnhImage')}
                id="user-account-cnhImage"
                name="cnhImage"
                data-cy="cnhImage"
                type="textarea"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.profileImage')}
                id="user-account-profileImage"
                name="profileImage"
                data-cy="profileImage"
                type="textarea"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.cnhExpirationDate')}
                id="user-account-cnhExpirationDate"
                name="cnhExpirationDate"
                data-cy="cnhExpirationDate"
                type="date"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.cnhStatus')}
                id="user-account-cnhStatus"
                name="cnhStatus"
                data-cy="cnhStatus"
                type="select"
              >
                {cNHSituationValues.map(cNHSituation => (
                  <option value={cNHSituation} key={cNHSituation}>
                    {translate('smartiotApp.CNHSituation.' + cNHSituation)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('smartiotApp.userAccount.registrationStatus')}
                id="user-account-registrationStatus"
                name="registrationStatus"
                data-cy="registrationStatus"
                type="select"
              >
                {registerSituationValues.map(registerSituation => (
                  <option value={registerSituation} key={registerSituation}>
                    {translate('smartiotApp.RegisterSituation.' + registerSituation)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('smartiotApp.userAccount.analyzedBy')}
                id="user-account-analyzedBy"
                name="analyzedBy"
                data-cy="analyzedBy"
                type="text"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.analyzedAt')}
                id="user-account-analyzedAt"
                name="analyzedAt"
                data-cy="analyzedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.signatureImage')}
                id="user-account-signatureImage"
                name="signatureImage"
                data-cy="signatureImage"
                type="textarea"
              />
              <ValidatedField
                label={translate('smartiotApp.userAccount.residenceProofImage')}
                id="user-account-residenceProofImage"
                name="residenceProofImage"
                data-cy="residenceProofImage"
                type="textarea"
              />
              <ValidatedField
                id="user-account-applicationUser"
                name="applicationUser"
                data-cy="applicationUser"
                label={translate('smartiotApp.userAccount.applicationUser')}
                type="select"
              >
                <option value="" key="0" />
                {applicationUsers
                  ? applicationUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-account" replace color="info">
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

export default UserAccountUpdate;
