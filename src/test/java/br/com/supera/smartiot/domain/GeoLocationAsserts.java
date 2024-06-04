package br.com.supera.smartiot.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class GeoLocationAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGeoLocationAllPropertiesEquals(GeoLocation expected, GeoLocation actual) {
        assertGeoLocationAutoGeneratedPropertiesEquals(expected, actual);
        assertGeoLocationAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGeoLocationAllUpdatablePropertiesEquals(GeoLocation expected, GeoLocation actual) {
        assertGeoLocationUpdatableFieldsEquals(expected, actual);
        assertGeoLocationUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGeoLocationAutoGeneratedPropertiesEquals(GeoLocation expected, GeoLocation actual) {
        assertThat(expected)
            .as("Verify GeoLocation auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGeoLocationUpdatableFieldsEquals(GeoLocation expected, GeoLocation actual) {
        assertThat(expected)
            .as("Verify GeoLocation relevant properties")
            .satisfies(e -> assertThat(e.getLatitude()).as("check latitude").isEqualTo(actual.getLatitude()))
            .satisfies(e -> assertThat(e.getLongitude()).as("check longitude").isEqualTo(actual.getLongitude()))
            .satisfies(e -> assertThat(e.getFullAddress()).as("check fullAddress").isEqualTo(actual.getFullAddress()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertGeoLocationUpdatableRelationshipsEquals(GeoLocation expected, GeoLocation actual) {}
}
