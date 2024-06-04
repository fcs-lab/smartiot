import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './storage-blob.reducer';

export const StorageBlobDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storageBlobEntity = useAppSelector(state => state.storageBlob.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storageBlobDetailsHeading">
          <Translate contentKey="smartiotApp.storageBlob.detail.title">StorageBlob</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.id}</dd>
          <dt>
            <span id="fileName">
              <Translate contentKey="smartiotApp.storageBlob.fileName">File Name</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.fileName}</dd>
          <dt>
            <span id="contentType">
              <Translate contentKey="smartiotApp.storageBlob.contentType">Content Type</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.contentType}</dd>
          <dt>
            <span id="byteSize">
              <Translate contentKey="smartiotApp.storageBlob.byteSize">Byte Size</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.byteSize}</dd>
          <dt>
            <span id="checksum">
              <Translate contentKey="smartiotApp.storageBlob.checksum">Checksum</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.checksum}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="smartiotApp.storageBlob.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {storageBlobEntity.createdAt ? <TextFormat value={storageBlobEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="smartiotApp.storageBlob.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {storageBlobEntity.updatedAt ? <TextFormat value={storageBlobEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="smartiotApp.storageBlob.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.lastModifiedBy}</dd>
          <dt>
            <span id="key">
              <Translate contentKey="smartiotApp.storageBlob.key">Key</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.key}</dd>
          <dt>
            <span id="metadata">
              <Translate contentKey="smartiotApp.storageBlob.metadata">Metadata</Translate>
            </span>
          </dt>
          <dd>{storageBlobEntity.metadata}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="smartiotApp.storageBlob.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {storageBlobEntity.deletedAt ? <TextFormat value={storageBlobEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/storage-blob" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/storage-blob/${storageBlobEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StorageBlobDetail;
