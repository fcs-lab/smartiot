import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-account.reducer';

export const UserAccountDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userAccountEntity = useAppSelector(state => state.userAccount.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userAccountDetailsHeading">
          <Translate contentKey="smartiotApp.userAccount.detail.title">UserAccount</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.id}</dd>
          <dt>
            <span id="accountName">
              <Translate contentKey="smartiotApp.userAccount.accountName">Account Name</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.accountName}</dd>
          <dt>
            <span id="emailAddress">
              <Translate contentKey="smartiotApp.userAccount.emailAddress">Email Address</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.emailAddress}</dd>
          <dt>
            <span id="admissionDate">
              <Translate contentKey="smartiotApp.userAccount.admissionDate">Admission Date</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.admissionDate ? (
              <TextFormat value={userAccountEntity.admissionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="smartiotApp.userAccount.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.createdAt ? <TextFormat value={userAccountEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="smartiotApp.userAccount.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.updatedAt ? <TextFormat value={userAccountEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="smartiotApp.userAccount.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="mobilePhone">
              <Translate contentKey="smartiotApp.userAccount.mobilePhone">Mobile Phone</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.mobilePhone}</dd>
          <dt>
            <span id="rpushFeedbackId">
              <Translate contentKey="smartiotApp.userAccount.rpushFeedbackId">Rpush Feedback Id</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.rpushFeedbackId}</dd>
          <dt>
            <span id="execCommands">
              <Translate contentKey="smartiotApp.userAccount.execCommands">Exec Commands</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.execCommands ? 'true' : 'false'}</dd>
          <dt>
            <span id="isBlocked">
              <Translate contentKey="smartiotApp.userAccount.isBlocked">Is Blocked</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.isBlocked ? 'true' : 'false'}</dd>
          <dt>
            <span id="employerName">
              <Translate contentKey="smartiotApp.userAccount.employerName">Employer Name</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.employerName}</dd>
          <dt>
            <span id="pushConfiguration">
              <Translate contentKey="smartiotApp.userAccount.pushConfiguration">Push Configuration</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.pushConfiguration}</dd>
          <dt>
            <span id="traveledDistance">
              <Translate contentKey="smartiotApp.userAccount.traveledDistance">Traveled Distance</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.traveledDistance}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="smartiotApp.userAccount.language">Language</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.language}</dd>
          <dt>
            <span id="blockedReason">
              <Translate contentKey="smartiotApp.userAccount.blockedReason">Blocked Reason</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.blockedReason}</dd>
          <dt>
            <span id="blockedById">
              <Translate contentKey="smartiotApp.userAccount.blockedById">Blocked By Id</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.blockedById}</dd>
          <dt>
            <span id="blockedAt">
              <Translate contentKey="smartiotApp.userAccount.blockedAt">Blocked At</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.blockedAt ? <TextFormat value={userAccountEntity.blockedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedReason">
              <Translate contentKey="smartiotApp.userAccount.deletedReason">Deleted Reason</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.deletedReason}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="smartiotApp.userAccount.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.deletedAt ? <TextFormat value={userAccountEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedById">
              <Translate contentKey="smartiotApp.userAccount.deletedById">Deleted By Id</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.deletedById}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="smartiotApp.userAccount.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.lastModifiedBy}</dd>
          <dt>
            <span id="registrationCode">
              <Translate contentKey="smartiotApp.userAccount.registrationCode">Registration Code</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.registrationCode}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="smartiotApp.userAccount.password">Password</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.password}</dd>
          <dt>
            <span id="passwordHint">
              <Translate contentKey="smartiotApp.userAccount.passwordHint">Password Hint</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.passwordHint}</dd>
          <dt>
            <span id="featureFlags">
              <Translate contentKey="smartiotApp.userAccount.featureFlags">Feature Flags</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.featureFlags}</dd>
          <dt>
            <span id="zipCode">
              <Translate contentKey="smartiotApp.userAccount.zipCode">Zip Code</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.zipCode}</dd>
          <dt>
            <span id="publicPlace">
              <Translate contentKey="smartiotApp.userAccount.publicPlace">Public Place</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.publicPlace}</dd>
          <dt>
            <span id="addressNumber">
              <Translate contentKey="smartiotApp.userAccount.addressNumber">Address Number</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.addressNumber}</dd>
          <dt>
            <span id="streetName">
              <Translate contentKey="smartiotApp.userAccount.streetName">Street Name</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.streetName}</dd>
          <dt>
            <span id="addressComplement">
              <Translate contentKey="smartiotApp.userAccount.addressComplement">Address Complement</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.addressComplement}</dd>
          <dt>
            <span id="cityName">
              <Translate contentKey="smartiotApp.userAccount.cityName">City Name</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.cityName}</dd>
          <dt>
            <span id="stateName">
              <Translate contentKey="smartiotApp.userAccount.stateName">State Name</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.stateName}</dd>
          <dt>
            <span id="cnhImage">
              <Translate contentKey="smartiotApp.userAccount.cnhImage">Cnh Image</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.cnhImage}</dd>
          <dt>
            <span id="profileImage">
              <Translate contentKey="smartiotApp.userAccount.profileImage">Profile Image</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.profileImage}</dd>
          <dt>
            <span id="cnhExpirationDate">
              <Translate contentKey="smartiotApp.userAccount.cnhExpirationDate">Cnh Expiration Date</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.cnhExpirationDate ? (
              <TextFormat value={userAccountEntity.cnhExpirationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="cnhStatus">
              <Translate contentKey="smartiotApp.userAccount.cnhStatus">Cnh Status</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.cnhStatus}</dd>
          <dt>
            <span id="registrationStatus">
              <Translate contentKey="smartiotApp.userAccount.registrationStatus">Registration Status</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.registrationStatus}</dd>
          <dt>
            <span id="analyzedBy">
              <Translate contentKey="smartiotApp.userAccount.analyzedBy">Analyzed By</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.analyzedBy}</dd>
          <dt>
            <span id="analyzedAt">
              <Translate contentKey="smartiotApp.userAccount.analyzedAt">Analyzed At</Translate>
            </span>
          </dt>
          <dd>
            {userAccountEntity.analyzedAt ? <TextFormat value={userAccountEntity.analyzedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="signatureImage">
              <Translate contentKey="smartiotApp.userAccount.signatureImage">Signature Image</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.signatureImage}</dd>
          <dt>
            <span id="residenceProofImage">
              <Translate contentKey="smartiotApp.userAccount.residenceProofImage">Residence Proof Image</Translate>
            </span>
          </dt>
          <dd>{userAccountEntity.residenceProofImage}</dd>
          <dt>
            <Translate contentKey="smartiotApp.userAccount.applicationUser">Application User</Translate>
          </dt>
          <dd>{userAccountEntity.applicationUser ? userAccountEntity.applicationUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-account" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-account/${userAccountEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserAccountDetail;
