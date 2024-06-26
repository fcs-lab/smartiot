package br.com.supera.smartiot.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AppDeviceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppDeviceAllPropertiesEquals(AppDevice expected, AppDevice actual) {
        assertAppDeviceAutoGeneratedPropertiesEquals(expected, actual);
        assertAppDeviceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppDeviceAllUpdatablePropertiesEquals(AppDevice expected, AppDevice actual) {
        assertAppDeviceUpdatableFieldsEquals(expected, actual);
        assertAppDeviceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppDeviceAutoGeneratedPropertiesEquals(AppDevice expected, AppDevice actual) {
        assertThat(expected)
            .as("Verify AppDevice auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppDeviceUpdatableFieldsEquals(AppDevice expected, AppDevice actual) {
        assertThat(expected)
            .as("Verify AppDevice relevant properties")
            .satisfies(e -> assertThat(e.getDeviceId()).as("check deviceId").isEqualTo(actual.getDeviceId()))
            .satisfies(e -> assertThat(e.getDeviceType()).as("check deviceType").isEqualTo(actual.getDeviceType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAppDeviceUpdatableRelationshipsEquals(AppDevice expected, AppDevice actual) {
        assertThat(expected)
            .as("Verify AppDevice relationships")
            .satisfies(e -> assertThat(e.getVehicle()).as("check vehicle").isEqualTo(actual.getVehicle()));
    }
}
