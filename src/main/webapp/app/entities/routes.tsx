import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AggregatedData from './aggregated-data';
import Alert from './alert';
import AppDevice from './app-device';
import ApplicationUser from './application-user';
import CarRide from './car-ride';
import ChatBooking from './chat-booking';
import ChatMessage from './chat-message';
import ChatSession from './chat-session';
import ChatUser from './chat-user';
import Cliente from './cliente';
import Company from './company';
import ConfiguracaoAlerta from './configuracao-alerta';
import Consumer from './consumer';
import CostCenter from './cost-center';
import DadoSensor from './dado-sensor';
import DeviceCommand from './device-command';
import DeviceTelemetry from './device-telemetry';
import Enrollment from './enrollment';
import GeoLocation from './geo-location';
import ManualEntry from './manual-entry';
import Measurement from './measurement';
import Notification from './notification';
import Payment from './payment';
import Pricing from './pricing';
import Report from './report';
import ResourceGroup from './resource-group';
import Sensor from './sensor';
import StorageAttachment from './storage-attachment';
import StorageBlob from './storage-blob';
import SystemAlert from './system-alert';
import UserAccount from './user-account';
import UserContract from './user-contract';
import UserDashboard from './user-dashboard';
import UserReport from './user-report';
import UserRole from './user-role';
import VehicleDamage from './vehicle-damage';
import VehicleGroup from './vehicle-group';
import VehicleInfo from './vehicle-info';
import VehicleManufacturer from './vehicle-manufacturer';
import VehicleModel from './vehicle-model';
import VehicleService from './vehicle-service';
import VehicleStatusLog from './vehicle-status-log';
import VehicleSubStatus from './vehicle-sub-status';
import WaterAlert from './water-alert';
import WaterMeasurement from './water-measurement';
import WaterSensor from './water-sensor';
import WaterUsageLog from './water-usage-log';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="aggregated-data/*" element={<AggregatedData />} />
        <Route path="alert/*" element={<Alert />} />
        <Route path="app-device/*" element={<AppDevice />} />
        <Route path="application-user/*" element={<ApplicationUser />} />
        <Route path="car-ride/*" element={<CarRide />} />
        <Route path="chat-booking/*" element={<ChatBooking />} />
        <Route path="chat-message/*" element={<ChatMessage />} />
        <Route path="chat-session/*" element={<ChatSession />} />
        <Route path="chat-user/*" element={<ChatUser />} />
        <Route path="cliente/*" element={<Cliente />} />
        <Route path="company/*" element={<Company />} />
        <Route path="configuracao-alerta/*" element={<ConfiguracaoAlerta />} />
        <Route path="consumer/*" element={<Consumer />} />
        <Route path="cost-center/*" element={<CostCenter />} />
        <Route path="dado-sensor/*" element={<DadoSensor />} />
        <Route path="device-command/*" element={<DeviceCommand />} />
        <Route path="device-telemetry/*" element={<DeviceTelemetry />} />
        <Route path="enrollment/*" element={<Enrollment />} />
        <Route path="geo-location/*" element={<GeoLocation />} />
        <Route path="manual-entry/*" element={<ManualEntry />} />
        <Route path="measurement/*" element={<Measurement />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="pricing/*" element={<Pricing />} />
        <Route path="report/*" element={<Report />} />
        <Route path="resource-group/*" element={<ResourceGroup />} />
        <Route path="sensor/*" element={<Sensor />} />
        <Route path="storage-attachment/*" element={<StorageAttachment />} />
        <Route path="storage-blob/*" element={<StorageBlob />} />
        <Route path="system-alert/*" element={<SystemAlert />} />
        <Route path="user-account/*" element={<UserAccount />} />
        <Route path="user-contract/*" element={<UserContract />} />
        <Route path="user-dashboard/*" element={<UserDashboard />} />
        <Route path="user-report/*" element={<UserReport />} />
        <Route path="user-role/*" element={<UserRole />} />
        <Route path="vehicle-damage/*" element={<VehicleDamage />} />
        <Route path="vehicle-group/*" element={<VehicleGroup />} />
        <Route path="vehicle-info/*" element={<VehicleInfo />} />
        <Route path="vehicle-manufacturer/*" element={<VehicleManufacturer />} />
        <Route path="vehicle-model/*" element={<VehicleModel />} />
        <Route path="vehicle-service/*" element={<VehicleService />} />
        <Route path="vehicle-status-log/*" element={<VehicleStatusLog />} />
        <Route path="vehicle-sub-status/*" element={<VehicleSubStatus />} />
        <Route path="water-alert/*" element={<WaterAlert />} />
        <Route path="water-measurement/*" element={<WaterMeasurement />} />
        <Route path="water-sensor/*" element={<WaterSensor />} />
        <Route path="water-usage-log/*" element={<WaterUsageLog />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
