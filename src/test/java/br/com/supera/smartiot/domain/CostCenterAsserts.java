package br.com.supera.smartiot.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CostCenterAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCostCenterAllPropertiesEquals(CostCenter expected, CostCenter actual) {
        assertCostCenterAutoGeneratedPropertiesEquals(expected, actual);
        assertCostCenterAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCostCenterAllUpdatablePropertiesEquals(CostCenter expected, CostCenter actual) {
        assertCostCenterUpdatableFieldsEquals(expected, actual);
        assertCostCenterUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCostCenterAutoGeneratedPropertiesEquals(CostCenter expected, CostCenter actual) {
        assertThat(expected)
            .as("Verify CostCenter auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCostCenterUpdatableFieldsEquals(CostCenter expected, CostCenter actual) {
        assertThat(expected)
            .as("Verify CostCenter relevant properties")
            .satisfies(e -> assertThat(e.getCenterName()).as("check centerName").isEqualTo(actual.getCenterName()))
            .satisfies(e -> assertThat(e.getBudgetAmount()).as("check budgetAmount").isEqualTo(actual.getBudgetAmount()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCostCenterUpdatableRelationshipsEquals(CostCenter expected, CostCenter actual) {}
}