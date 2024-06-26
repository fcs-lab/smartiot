package br.com.supera.smartiot.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class WaterMeasurementAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWaterMeasurementAllPropertiesEquals(WaterMeasurement expected, WaterMeasurement actual) {
        assertWaterMeasurementAutoGeneratedPropertiesEquals(expected, actual);
        assertWaterMeasurementAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWaterMeasurementAllUpdatablePropertiesEquals(WaterMeasurement expected, WaterMeasurement actual) {
        assertWaterMeasurementUpdatableFieldsEquals(expected, actual);
        assertWaterMeasurementUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWaterMeasurementAutoGeneratedPropertiesEquals(WaterMeasurement expected, WaterMeasurement actual) {
        assertThat(expected)
            .as("Verify WaterMeasurement auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWaterMeasurementUpdatableFieldsEquals(WaterMeasurement expected, WaterMeasurement actual) {
        assertThat(expected)
            .as("Verify WaterMeasurement relevant properties")
            .satisfies(e -> assertThat(e.getMeasurementDate()).as("check measurementDate").isEqualTo(actual.getMeasurementDate()))
            .satisfies(e -> assertThat(e.getWaterLevel()).as("check waterLevel").isEqualTo(actual.getWaterLevel()))
            .satisfies(e -> assertThat(e.getWaterQuality()).as("check waterQuality").isEqualTo(actual.getWaterQuality()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertWaterMeasurementUpdatableRelationshipsEquals(WaterMeasurement expected, WaterMeasurement actual) {}
}
