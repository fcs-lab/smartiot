import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sensor.reducer';

export const SensorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const sensorEntity = useAppSelector(state => state.sensor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="sensorDetailsHeading">
          <Translate contentKey="smartiotApp.sensor.detail.title">Sensor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{sensorEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="smartiotApp.sensor.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{sensorEntity.nome}</dd>
          <dt>
            <span id="tipo">
              <Translate contentKey="smartiotApp.sensor.tipo">Tipo</Translate>
            </span>
          </dt>
          <dd>{sensorEntity.tipo}</dd>
          <dt>
            <span id="configuracao">
              <Translate contentKey="smartiotApp.sensor.configuracao">Configuracao</Translate>
            </span>
          </dt>
          <dd>{sensorEntity.configuracao}</dd>
          <dt>
            <Translate contentKey="smartiotApp.sensor.cliente">Cliente</Translate>
          </dt>
          <dd>{sensorEntity.cliente ? sensorEntity.cliente.nome : ''}</dd>
          <dt>
            <Translate contentKey="smartiotApp.sensor.dadoSensores">Dado Sensores</Translate>
          </dt>
          <dd>{sensorEntity.dadoSensores ? sensorEntity.dadoSensores.timestamp : ''}</dd>
        </dl>
        <Button tag={Link} to="/sensor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sensor/${sensorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SensorDetail;
