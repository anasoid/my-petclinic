package org.anasoid.petclinic.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecialtyAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialtyAllPropertiesEquals(Specialty expected, Specialty actual) {
        assertSpecialtyAutoGeneratedPropertiesEquals(expected, actual);
        assertSpecialtyAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialtyAllUpdatablePropertiesEquals(Specialty expected, Specialty actual) {
        assertSpecialtyUpdatableFieldsEquals(expected, actual);
        assertSpecialtyUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialtyAutoGeneratedPropertiesEquals(Specialty expected, Specialty actual) {
        assertThat(expected)
            .as("Verify Specialty auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialtyUpdatableFieldsEquals(Specialty expected, Specialty actual) {
        assertThat(expected)
            .as("Verify Specialty relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSpecialtyUpdatableRelationshipsEquals(Specialty expected, Specialty actual) {
        assertThat(expected)
            .as("Verify Specialty relationships")
            .satisfies(e -> assertThat(e.getVets()).as("check vets").isEqualTo(actual.getVets()));
    }
}
