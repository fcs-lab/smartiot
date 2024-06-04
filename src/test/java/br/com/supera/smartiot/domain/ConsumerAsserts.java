package br.com.supera.smartiot.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsumerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConsumerAllPropertiesEquals(Consumer expected, Consumer actual) {
        assertConsumerAutoGeneratedPropertiesEquals(expected, actual);
        assertConsumerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConsumerAllUpdatablePropertiesEquals(Consumer expected, Consumer actual) {
        assertConsumerUpdatableFieldsEquals(expected, actual);
        assertConsumerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConsumerAutoGeneratedPropertiesEquals(Consumer expected, Consumer actual) {
        assertThat(expected)
            .as("Verify Consumer auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConsumerUpdatableFieldsEquals(Consumer expected, Consumer actual) {
        assertThat(expected)
            .as("Verify Consumer relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getStreet()).as("check street").isEqualTo(actual.getStreet()))
            .satisfies(e -> assertThat(e.getNeighborhood()).as("check neighborhood").isEqualTo(actual.getNeighborhood()))
            .satisfies(e -> assertThat(e.getPropertyNumber()).as("check propertyNumber").isEqualTo(actual.getPropertyNumber()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertConsumerUpdatableRelationshipsEquals(Consumer expected, Consumer actual) {}
}
