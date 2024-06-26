package com.veterinary.sistema.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioAllPropertiesEquals(Usuario expected, Usuario actual) {
        assertUsuarioAutoGeneratedPropertiesEquals(expected, actual);
        assertUsuarioAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioAllUpdatablePropertiesEquals(Usuario expected, Usuario actual) {
        assertUsuarioUpdatableFieldsEquals(expected, actual);
        assertUsuarioUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioAutoGeneratedPropertiesEquals(Usuario expected, Usuario actual) {
        assertThat(expected)
            .as("Verify Usuario auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioUpdatableFieldsEquals(Usuario expected, Usuario actual) {
        assertThat(expected)
            .as("Verify Usuario relevant properties")
            .satisfies(e -> assertThat(e.getNombreUsuario()).as("check nombreUsuario").isEqualTo(actual.getNombreUsuario()))
            .satisfies(e -> assertThat(e.getRol()).as("check rol").isEqualTo(actual.getRol()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsuarioUpdatableRelationshipsEquals(Usuario expected, Usuario actual) {
        assertThat(expected)
            .as("Verify Usuario relationships")
            .satisfies(e -> assertThat(e.getEstetica()).as("check estetica").isEqualTo(actual.getEstetica()))
            .satisfies(e -> assertThat(e.getVeterinario()).as("check veterinario").isEqualTo(actual.getVeterinario()))
            .satisfies(e -> assertThat(e.getDueno()).as("check dueno").isEqualTo(actual.getDueno()));
    }
}
