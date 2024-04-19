package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.TratamientoAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Tratamiento;
import com.veterinary.sistema.repository.TratamientoRepository;
import com.veterinary.sistema.service.dto.TratamientoDTO;
import com.veterinary.sistema.service.mapper.TratamientoMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TratamientoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TratamientoResourceIT {

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTAS = "AAAAAAAAAA";
    private static final String UPDATED_NOTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tratamientos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private TratamientoMapper tratamientoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTratamientoMockMvc;

    private Tratamiento tratamiento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tratamiento createEntity(EntityManager em) {
        Tratamiento tratamiento = new Tratamiento().fechaInicio(DEFAULT_FECHA_INICIO).fechaFin(DEFAULT_FECHA_FIN).notas(DEFAULT_NOTAS);
        return tratamiento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tratamiento createUpdatedEntity(EntityManager em) {
        Tratamiento tratamiento = new Tratamiento().fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).notas(UPDATED_NOTAS);
        return tratamiento;
    }

    @BeforeEach
    public void initTest() {
        tratamiento = createEntity(em);
    }

    @Test
    @Transactional
    void createTratamiento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);
        var returnedTratamientoDTO = om.readValue(
            restTratamientoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TratamientoDTO.class
        );

        // Validate the Tratamiento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTratamiento = tratamientoMapper.toEntity(returnedTratamientoDTO);
        assertTratamientoUpdatableFieldsEquals(returnedTratamiento, getPersistedTratamiento(returnedTratamiento));
    }

    @Test
    @Transactional
    void createTratamientoWithExistingId() throws Exception {
        // Create the Tratamiento with an existing ID
        tratamiento.setId(1L);
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tratamiento.setFechaInicio(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tratamiento.setFechaFin(null);

        // Create the Tratamiento, which fails.
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        restTratamientoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTratamientos() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        // Get all the tratamientoList
        restTratamientoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamiento.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].notas").value(hasItem(DEFAULT_NOTAS)));
    }

    @Test
    @Transactional
    void getTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        // Get the tratamiento
        restTratamientoMockMvc
            .perform(get(ENTITY_API_URL_ID, tratamiento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tratamiento.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.notas").value(DEFAULT_NOTAS));
    }

    @Test
    @Transactional
    void getNonExistingTratamiento() throws Exception {
        // Get the tratamiento
        restTratamientoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tratamiento
        Tratamiento updatedTratamiento = tratamientoRepository.findById(tratamiento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTratamiento are not directly saved in db
        em.detach(updatedTratamiento);
        updatedTratamiento.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).notas(UPDATED_NOTAS);
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(updatedTratamiento);

        restTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tratamientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTratamientoToMatchAllProperties(updatedTratamiento);
    }

    @Test
    @Transactional
    void putNonExistingTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tratamientoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTratamientoWithPatch() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tratamiento using partial update
        Tratamiento partialUpdatedTratamiento = new Tratamiento();
        partialUpdatedTratamiento.setId(tratamiento.getId());

        partialUpdatedTratamiento.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).notas(UPDATED_NOTAS);

        restTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTratamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTratamiento))
            )
            .andExpect(status().isOk());

        // Validate the Tratamiento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTratamientoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTratamiento, tratamiento),
            getPersistedTratamiento(tratamiento)
        );
    }

    @Test
    @Transactional
    void fullUpdateTratamientoWithPatch() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tratamiento using partial update
        Tratamiento partialUpdatedTratamiento = new Tratamiento();
        partialUpdatedTratamiento.setId(tratamiento.getId());

        partialUpdatedTratamiento.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN).notas(UPDATED_NOTAS);

        restTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTratamiento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTratamiento))
            )
            .andExpect(status().isOk());

        // Validate the Tratamiento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTratamientoUpdatableFieldsEquals(partialUpdatedTratamiento, getPersistedTratamiento(partialUpdatedTratamiento));
    }

    @Test
    @Transactional
    void patchNonExistingTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tratamientoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tratamientoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTratamiento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tratamiento.setId(longCount.incrementAndGet());

        // Create the Tratamiento
        TratamientoDTO tratamientoDTO = tratamientoMapper.toDto(tratamiento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tratamientoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tratamiento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTratamiento() throws Exception {
        // Initialize the database
        tratamientoRepository.saveAndFlush(tratamiento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tratamiento
        restTratamientoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tratamiento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tratamientoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Tratamiento getPersistedTratamiento(Tratamiento tratamiento) {
        return tratamientoRepository.findById(tratamiento.getId()).orElseThrow();
    }

    protected void assertPersistedTratamientoToMatchAllProperties(Tratamiento expectedTratamiento) {
        assertTratamientoAllPropertiesEquals(expectedTratamiento, getPersistedTratamiento(expectedTratamiento));
    }

    protected void assertPersistedTratamientoToMatchUpdatableProperties(Tratamiento expectedTratamiento) {
        assertTratamientoAllUpdatablePropertiesEquals(expectedTratamiento, getPersistedTratamiento(expectedTratamiento));
    }
}
