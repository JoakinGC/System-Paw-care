package com.veterinary.sistema.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FactoresAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactoresAllPropertiesEquals(Factores expected, Factores actual) {
        assertFactoresAutoGeneratedPropertiesEquals(expected, actual);
        assertFactoresAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactoresAllUpdatablePropertiesEquals(Factores expected, Factores actual) {
        assertFactoresUpdatableFieldsEquals(expected, actual);
        assertFactoresUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactoresAutoGeneratedPropertiesEquals(Factores expected, Factores actual) {
        assertThat(expected)
            .as("Verify Factores auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactoresUpdatableFieldsEquals(Factores expected, Factores actual) {
        assertThat(expected)
            .as("Verify Factores relevant properties")
            .satisfies(e -> assertThat(e.getNombre()).as("check nombre").isEqualTo(actual.getNombre()))
            .satisfies(e -> assertThat(e.getTipo()).as("check tipo").isEqualTo(actual.getTipo()))
            .satisfies(e -> assertThat(e.getDescripcion()).as("check descripcion").isEqualTo(actual.getDescripcion()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFactoresUpdatableRelationshipsEquals(Factores expected, Factores actual) {
        assertThat(expected)
            .as("Verify Factores relationships")
            .satisfies(e -> assertThat(e.getEnfermedads()).as("check enfermedads").isEqualTo(actual.getEnfermedads()));
    }
}
