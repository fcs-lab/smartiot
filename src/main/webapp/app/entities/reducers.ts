import aggregatedData from 'app/entities/aggregated-data/aggregated-data.reducer';
import alert from 'app/entities/alert/alert.reducer';
import appDevice from 'app/entities/app-device/app-device.reducer';
import applicationUser from 'app/entities/application-user/application-user.reducer';
import carRide from 'app/entities/car-ride/car-ride.reducer';
import chatBooking from 'app/entities/chat-booking/chat-booking.reducer';
import chatMessage from 'app/entities/chat-message/chat-message.reducer';
import chatSession from 'app/entities/chat-session/chat-session.reducer';
import chatUser from 'app/entities/chat-user/chat-user.reducer';
import cliente from 'app/entities/cliente/cliente.reducer';
import company from 'app/entities/company/company.reducer';
import configuracaoAlerta from 'app/entities/configuracao-alerta/configuracao-alerta.reducer';
import consumer from 'app/entities/consumer/consumer.reducer';
import costCenter from 'app/entities/cost-center/cost-center.reducer';
import dadoSensor from 'app/entities/dado-sensor/dado-sensor.reducer';
import deviceCommand from 'app/entities/device-command/device-command.reducer';
import deviceTelemetry from 'app/entities/device-telemetry/device-telemetry.reducer';
import enrollment from 'app/entities/enrollment/enrollment.reducer';
import geoLocation from 'app/entities/geo-location/geo-location.reducer';
import manualEntry from 'app/entities/manual-entry/manual-entry.reducer';
import measurement from 'app/entities/measurement/measurement.reducer';
import notification from 'app/entities/notification/notification.reducer';
import payment from 'app/entities/payment/payment.reducer';
import pricing from 'app/entities/pricing/pricing.reducer';
import report from 'app/entities/report/report.reducer';
import resourceGroup from 'app/entities/resource-group/resource-group.reducer';
import sensor from 'app/entities/sensor/sensor.reducer';
import storageAttachment from 'app/entities/storage-attachment/storage-attachment.reducer';
import storageBlob from 'app/entities/storage-blob/storage-blob.reducer';
import systemAlert from 'app/entities/system-alert/system-alert.reducer';
import userAccount from 'app/entities/user-account/user-account.reducer';
import userContract from 'app/entities/user-contract/user-contract.reducer';
import userDashboard from 'app/entities/user-dashboard/user-dashboard.reducer';
import userReport from 'app/entities/user-report/user-report.reducer';
import userRole from 'app/entities/user-role/user-role.reducer';
import vehicleDamage from 'app/entities/vehicle-damage/vehicle-damage.reducer';
import vehicleGroup from 'app/entities/vehicle-group/vehicle-group.reducer';
import vehicleInfo from 'app/entities/vehicle-info/vehicle-info.reducer';
import vehicleManufacturer from 'app/entities/vehicle-manufacturer/vehicle-manufacturer.reducer';
import vehicleModel from 'app/entities/vehicle-model/vehicle-model.reducer';
import vehicleService from 'app/entities/vehicle-service/vehicle-service.reducer';
import vehicleStatusLog from 'app/entities/vehicle-status-log/vehicle-status-log.reducer';
import vehicleSubStatus from 'app/entities/vehicle-sub-status/vehicle-sub-status.reducer';
import waterAlert from 'app/entities/water-alert/water-alert.reducer';
import waterMeasurement from 'app/entities/water-measurement/water-measurement.reducer';
import waterSensor from 'app/entities/water-sensor/water-sensor.reducer';
import waterUsageLog from 'app/entities/water-usage-log/water-usage-log.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  aggregatedData,
  alert,
  appDevice,
  applicationUser,
  carRide,
  chatBooking,
  chatMessage,
  chatSession,
  chatUser,
  cliente,
  company,
  configuracaoAlerta,
  consumer,
  costCenter,
  dadoSensor,
  deviceCommand,
  deviceTelemetry,
  enrollment,
  geoLocation,
  manualEntry,
  measurement,
  notification,
  payment,
  pricing,
  report,
  resourceGroup,
  sensor,
  storageAttachment,
  storageBlob,
  systemAlert,
  userAccount,
  userContract,
  userDashboard,
  userReport,
  userRole,
  vehicleDamage,
  vehicleGroup,
  vehicleInfo,
  vehicleManufacturer,
  vehicleModel,
  vehicleService,
  vehicleStatusLog,
  vehicleSubStatus,
  waterAlert,
  waterMeasurement,
  waterSensor,
  waterUsageLog,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
