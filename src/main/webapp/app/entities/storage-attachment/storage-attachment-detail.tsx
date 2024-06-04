import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './storage-attachment.reducer';

export const StorageAttachmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storageAttachmentEntity = useAppSelector(state => state.storageAttachment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storageAttachmentDetailsHeading">
          <Translate contentKey="smartiotApp.storageAttachment.detail.title">StorageAttachment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storageAttachmentEntity.id}</dd>
          <dt>
            <span id="attachmentName">
              <Translate contentKey="smartiotApp.storageAttachment.attachmentName">Attachment Name</Translate>
            </span>
          </dt>
          <dd>{storageAttachmentEntity.attachmentName}</dd>
          <dt>
            <span id="recordType">
              <Translate contentKey="smartiotApp.storageAttachment.recordType">Record Type</Translate>
            </span>
          </dt>
          <dd>{storageAttachmentEntity.recordType}</dd>
          <dt>
            <span id="recordId">
              <Translate contentKey="smartiotApp.storageAttachment.recordId">Record Id</Translate>
            </span>
          </dt>
          <dd>{storageAttachmentEntity.recordId}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="smartiotApp.storageAttachment.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {storageAttachmentEntity.createdAt ? (
              <TextFormat value={storageAttachmentEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="smartiotApp.storageAttachment.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {storageAttachmentEntity.updatedAt ? (
              <TextFormat value={storageAttachmentEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="smartiotApp.storageAttachment.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{storageAttachmentEntity.lastModifiedBy}</dd>
          <dt>
            <span id="blobId">
              <Translate contentKey="smartiotApp.storageAttachment.blobId">Blob Id</Translate>
            </span>
          </dt>
          <dd>{storageAttachmentEntity.blobId}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="smartiotApp.storageAttachment.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {storageAttachmentEntity.deletedAt ? (
              <TextFormat value={storageAttachmentEntity.deletedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/storage-attachment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/storage-attachment/${storageAttachmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StorageAttachmentDetail;
