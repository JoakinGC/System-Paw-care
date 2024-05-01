package com.veterinary.sistema.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class MascotaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMascotaAllPropertiesEquals(Mascota expected, Mascota actual) {
        assertMascotaAutoGeneratedPropertiesEquals(expected, actual);
        assertMascotaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMascotaAllUpdatablePropertiesEquals(Mascota expected, Mascota actual) {
        assertMascotaUpdatableFieldsEquals(expected, actual);
        assertMascotaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMascotaAutoGeneratedPropertiesEquals(Mascota expected, Mascota actual) {
        assertThat(expected)
            .as("Verify Mascota auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMascotaUpdatableFieldsEquals(Mascota expected, Mascota actual) {
        assertThat(expected)
            .as("Verify Mascota relevant properties")
            .satisfies(
                e -> assertThat(e.getnIdentificacionCarnet()).as("check nIdentificacionCarnet").isEqualTo(actual.getnIdentificacionCarnet())
            )
            .satisfies(e -> assertThat(e.getFoto()).as("check foto").isEqualTo(actual.getFoto()))
            .satisfies(e -> assertThat(e.getFechaNacimiento()).as("check fechaNacimiento").isEqualTo(actual.getFechaNacimiento()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertMascotaUpdatableRelationshipsEquals(Mascota expected, Mascota actual) {
        assertThat(expected)
            .as("Verify Mascota relationships")
            .satisfies(e -> assertThat(e.getCitas()).as("check citas").isEqualTo(actual.getCitas()))
            .satisfies(e -> assertThat(e.getDueno()).as("check dueno").isEqualTo(actual.getDueno()))
            .satisfies(e -> assertThat(e.getEspecie()).as("check especie").isEqualTo(actual.getEspecie()))
            .satisfies(e -> assertThat(e.getRaza()).as("check raza").isEqualTo(actual.getRaza()));
    }
}