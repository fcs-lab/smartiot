import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/aggregated-data">
        <Translate contentKey="global.menu.entities.aggregatedData" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/alert">
        <Translate contentKey="global.menu.entities.alert" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app-device">
        <Translate contentKey="global.menu.entities.appDevice" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/application-user">
        <Translate contentKey="global.menu.entities.applicationUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/car-ride">
        <Translate contentKey="global.menu.entities.carRide" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chat-booking">
        <Translate contentKey="global.menu.entities.chatBooking" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chat-message">
        <Translate contentKey="global.menu.entities.chatMessage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chat-session">
        <Translate contentKey="global.menu.entities.chatSession" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chat-user">
        <Translate contentKey="global.menu.entities.chatUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cliente">
        <Translate contentKey="global.menu.entities.cliente" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/configuracao-alerta">
        <Translate contentKey="global.menu.entities.configuracaoAlerta" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/consumer">
        <Translate contentKey="global.menu.entities.consumer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cost-center">
        <Translate contentKey="global.menu.entities.costCenter" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/dado-sensor">
        <Translate contentKey="global.menu.entities.dadoSensor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/device-command">
        <Translate contentKey="global.menu.entities.deviceCommand" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/device-telemetry">
        <Translate contentKey="global.menu.entities.deviceTelemetry" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/enrollment">
        <Translate contentKey="global.menu.entities.enrollment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/geo-location">
        <Translate contentKey="global.menu.entities.geoLocation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/manual-entry">
        <Translate contentKey="global.menu.entities.manualEntry" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/measurement">
        <Translate contentKey="global.menu.entities.measurement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification">
        <Translate contentKey="global.menu.entities.notification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment">
        <Translate contentKey="global.menu.entities.payment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pricing">
        <Translate contentKey="global.menu.entities.pricing" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/report">
        <Translate contentKey="global.menu.entities.report" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/resource-group">
        <Translate contentKey="global.menu.entities.resourceGroup" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sensor">
        <Translate contentKey="global.menu.entities.sensor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/storage-attachment">
        <Translate contentKey="global.menu.entities.storageAttachment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/storage-blob">
        <Translate contentKey="global.menu.entities.storageBlob" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/system-alert">
        <Translate contentKey="global.menu.entities.systemAlert" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-account">
        <Translate contentKey="global.menu.entities.userAccount" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-contract">
        <Translate contentKey="global.menu.entities.userContract" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-dashboard">
        <Translate contentKey="global.menu.entities.userDashboard" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-report">
        <Translate contentKey="global.menu.entities.userReport" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-role">
        <Translate contentKey="global.menu.entities.userRole" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-damage">
        <Translate contentKey="global.menu.entities.vehicleDamage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-group">
        <Translate contentKey="global.menu.entities.vehicleGroup" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-info">
        <Translate contentKey="global.menu.entities.vehicleInfo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-manufacturer">
        <Translate contentKey="global.menu.entities.vehicleManufacturer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-model">
        <Translate contentKey="global.menu.entities.vehicleModel" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-service">
        <Translate contentKey="global.menu.entities.vehicleService" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-status-log">
        <Translate contentKey="global.menu.entities.vehicleStatusLog" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-sub-status">
        <Translate contentKey="global.menu.entities.vehicleSubStatus" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/water-alert">
        <Translate contentKey="global.menu.entities.waterAlert" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/water-measurement">
        <Translate contentKey="global.menu.entities.waterMeasurement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/water-sensor">
        <Translate contentKey="global.menu.entities.waterSensor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/water-usage-log">
        <Translate contentKey="global.menu.entities.waterUsageLog" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
