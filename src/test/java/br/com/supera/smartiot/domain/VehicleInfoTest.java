package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.AppDeviceTestSamples.*;
import static br.com.supera.smartiot.domain.ChatBookingTestSamples.*;
import static br.com.supera.smartiot.domain.SystemAlertTestSamples.*;
import static br.com.supera.smartiot.domain.VehicleDamageTestSamples.*;
import static br.com.supera.smartiot.domain.VehicleInfoTestSamples.*;
import static br.com.supera.smartiot.domain.VehicleServiceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VehicleInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleInfo.class);
        VehicleInfo vehicleInfo1 = getVehicleInfoSample1();
        VehicleInfo vehicleInfo2 = new VehicleInfo();
        assertThat(vehicleInfo1).isNotEqualTo(vehicleInfo2);

        vehicleInfo2.setId(vehicleInfo1.getId());
        assertThat(vehicleInfo1).isEqualTo(vehicleInfo2);

        vehicleInfo2 = getVehicleInfoSample2();
        assertThat(vehicleInfo1).isNotEqualTo(vehicleInfo2);
    }

    @Test
    void damagesTest() {
        VehicleInfo vehicleInfo = getVehicleInfoRandomSampleGenerator();
        VehicleDamage vehicleDamageBack = getVehicleDamageRandomSampleGenerator();

        vehicleInfo.addDamages(vehicleDamageBack);
        assertThat(vehicleInfo.getDamages()).containsOnly(vehicleDamageBack);
        assertThat(vehicleDamageBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.removeDamages(vehicleDamageBack);
        assertThat(vehicleInfo.getDamages()).doesNotContain(vehicleDamageBack);
        assertThat(vehicleDamageBack.getVehicle()).isNull();

        vehicleInfo.damages(new HashSet<>(Set.of(vehicleDamageBack)));
        assertThat(vehicleInfo.getDamages()).containsOnly(vehicleDamageBack);
        assertThat(vehicleDamageBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.setDamages(new HashSet<>());
        assertThat(vehicleInfo.getDamages()).doesNotContain(vehicleDamageBack);
        assertThat(vehicleDamageBack.getVehicle()).isNull();
    }

    @Test
    void reservationsTest() {
        VehicleInfo vehicleInfo = getVehicleInfoRandomSampleGenerator();
        ChatBooking chatBookingBack = getChatBookingRandomSampleGenerator();

        vehicleInfo.addReservations(chatBookingBack);
        assertThat(vehicleInfo.getReservations()).containsOnly(chatBookingBack);
        assertThat(chatBookingBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.removeReservations(chatBookingBack);
        assertThat(vehicleInfo.getReservations()).doesNotContain(chatBookingBack);
        assertThat(chatBookingBack.getVehicle()).isNull();

        vehicleInfo.reservations(new HashSet<>(Set.of(chatBookingBack)));
        assertThat(vehicleInfo.getReservations()).containsOnly(chatBookingBack);
        assertThat(chatBookingBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.setReservations(new HashSet<>());
        assertThat(vehicleInfo.getReservations()).doesNotContain(chatBookingBack);
        assertThat(chatBookingBack.getVehicle()).isNull();
    }

    @Test
    void servicesTest() {
        VehicleInfo vehicleInfo = getVehicleInfoRandomSampleGenerator();
        VehicleService vehicleServiceBack = getVehicleServiceRandomSampleGenerator();

        vehicleInfo.addServices(vehicleServiceBack);
        assertThat(vehicleInfo.getServices()).containsOnly(vehicleServiceBack);
        assertThat(vehicleServiceBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.removeServices(vehicleServiceBack);
        assertThat(vehicleInfo.getServices()).doesNotContain(vehicleServiceBack);
        assertThat(vehicleServiceBack.getVehicle()).isNull();

        vehicleInfo.services(new HashSet<>(Set.of(vehicleServiceBack)));
        assertThat(vehicleInfo.getServices()).containsOnly(vehicleServiceBack);
        assertThat(vehicleServiceBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.setServices(new HashSet<>());
        assertThat(vehicleInfo.getServices()).doesNotContain(vehicleServiceBack);
        assertThat(vehicleServiceBack.getVehicle()).isNull();
    }

    @Test
    void alertsTest() {
        VehicleInfo vehicleInfo = getVehicleInfoRandomSampleGenerator();
        SystemAlert systemAlertBack = getSystemAlertRandomSampleGenerator();

        vehicleInfo.addAlerts(systemAlertBack);
        assertThat(vehicleInfo.getAlerts()).containsOnly(systemAlertBack);
        assertThat(systemAlertBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.removeAlerts(systemAlertBack);
        assertThat(vehicleInfo.getAlerts()).doesNotContain(systemAlertBack);
        assertThat(systemAlertBack.getVehicle()).isNull();

        vehicleInfo.alerts(new HashSet<>(Set.of(systemAlertBack)));
        assertThat(vehicleInfo.getAlerts()).containsOnly(systemAlertBack);
        assertThat(systemAlertBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.setAlerts(new HashSet<>());
        assertThat(vehicleInfo.getAlerts()).doesNotContain(systemAlertBack);
        assertThat(systemAlertBack.getVehicle()).isNull();
    }

    @Test
    void devicesTest() {
        VehicleInfo vehicleInfo = getVehicleInfoRandomSampleGenerator();
        AppDevice appDeviceBack = getAppDeviceRandomSampleGenerator();

        vehicleInfo.addDevices(appDeviceBack);
        assertThat(vehicleInfo.getDevices()).containsOnly(appDeviceBack);
        assertThat(appDeviceBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.removeDevices(appDeviceBack);
        assertThat(vehicleInfo.getDevices()).doesNotContain(appDeviceBack);
        assertThat(appDeviceBack.getVehicle()).isNull();

        vehicleInfo.devices(new HashSet<>(Set.of(appDeviceBack)));
        assertThat(vehicleInfo.getDevices()).containsOnly(appDeviceBack);
        assertThat(appDeviceBack.getVehicle()).isEqualTo(vehicleInfo);

        vehicleInfo.setDevices(new HashSet<>());
        assertThat(vehicleInfo.getDevices()).doesNotContain(appDeviceBack);
        assertThat(appDeviceBack.getVehicle()).isNull();
    }
}
