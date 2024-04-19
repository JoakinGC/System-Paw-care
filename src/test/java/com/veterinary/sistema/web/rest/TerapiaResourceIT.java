package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.TerapiaAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Terapia;
import com.veterinary.sistema.repository.TerapiaRepository;
import com.veterinary.sistema.service.dto.TerapiaDTO;
import com.veterinary.sistema.service.mapper.TerapiaMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link TerapiaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TerapiaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/terapias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TerapiaRepository terapiaRepository;

    @Autowired
    private TerapiaMapper terapiaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTerapiaMockMvc;

    private Terapia terapia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terapia createEntity(EntityManager em) {
        Terapia terapia = new Terapia().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return terapia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terapia createUpdatedEntity(EntityManager em) {
        Terapia terapia = new Terapia().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return terapia;
    }

    @BeforeEach
    public void initTest() {
        terapia = createEntity(em);
    }

    @Test
    @Transactional
    void createTerapia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Terapia
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(terapia);
        var returnedTerapiaDTO = om.readValue(
            restTerapiaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(terapiaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TerapiaDTO.class
        );

        // Validate the Terapia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTerapia = terapiaMapper.toEntity(returnedTerapiaDTO);
        assertTerapiaUpdatableFieldsEquals(returnedTerapia, getPersistedTerapia(returnedTerapia));
    }

    @Test
    @Transactional
    void createTerapiaWithExistingId() throws Exception {
        // Create the Terapia with an existing ID
        terapia.setId(1L);
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(terapia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerapiaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(terapiaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Terapia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTerapias() throws Exception {
        // Initialize the database
        terapiaRepository.saveAndFlush(terapia);

        // Get all the terapiaList
        restTerapiaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terapia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getTerapia() throws Exception {
        // Initialize the database
        terapiaRepository.saveAndFlush(terapia);

        // Get the terapia
        restTerapiaMockMvc
            .perform(get(ENTITY_API_URL_ID, terapia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(terapia.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingTerapia() throws Exception {
        // Get the terapia
        restTerapiaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTerapia() throws Exception {
        // Initialize the database
        terapiaRepository.saveAndFlush(terapia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the terapia
        Terapia updatedTerapia = terapiaRepository.findById(terapia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTerapia are not directly saved in db
        em.detach(updatedTerapia);
        updatedTerapia.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(updatedTerapia);

        restTerapiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terapiaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(terapiaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Terapia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTerapiaToMatchAllProperties(updatedTerapia);
    }

    @Test
    @Transactional
    void putNonExistingTerapia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        terapia.setId(longCount.incrementAndGet());

        // Create the Terapia
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(terapia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerapiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terapiaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(terapiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terapia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTerapia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        terapia.setId(longCount.incrementAndGet());

        // Create the Terapia
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(terapia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerapiaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(terapiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terapia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTerapia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        terapia.setId(longCount.incrementAndGet());

        // Create the Terapia
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(terapia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerapiaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(terapiaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Terapia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTerapiaWithPatch() throws Exception {
        // Initialize the database
        terapiaRepository.saveAndFlush(terapia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the terapia using partial update
        Terapia partialUpdatedTerapia = new Terapia();
        partialUpdatedTerapia.setId(terapia.getId());

        partialUpdatedTerapia.descripcion(UPDATED_DESCRIPCION);

        restTerapiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerapia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTerapia))
            )
            .andExpect(status().isOk());

        // Validate the Terapia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTerapiaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTerapia, terapia), getPersistedTerapia(terapia));
    }

    @Test
    @Transactional
    void fullUpdateTerapiaWithPatch() throws Exception {
        // Initialize the database
        terapiaRepository.saveAndFlush(terapia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the terapia using partial update
        Terapia partialUpdatedTerapia = new Terapia();
        partialUpdatedTerapia.setId(terapia.getId());

        partialUpdatedTerapia.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restTerapiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerapia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTerapia))
            )
            .andExpect(status().isOk());

        // Validate the Terapia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTerapiaUpdatableFieldsEquals(partialUpdatedTerapia, getPersistedTerapia(partialUpdatedTerapia));
    }

    @Test
    @Transactional
    void patchNonExistingTerapia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        terapia.setId(longCount.incrementAndGet());

        // Create the Terapia
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(terapia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerapiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, terapiaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(terapiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terapia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTerapia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        terapia.setId(longCount.incrementAndGet());

        // Create the Terapia
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(terapia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerapiaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(terapiaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Terapia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTerapia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        terapia.setId(longCount.incrementAndGet());

        // Create the Terapia
        TerapiaDTO terapiaDTO = terapiaMapper.toDto(terapia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerapiaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(terapiaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Terapia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTerapia() throws Exception {
        // Initialize the database
        terapiaRepository.saveAndFlush(terapia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the terapia
        restTerapiaMockMvc
            .perform(delete(ENTITY_API_URL_ID, terapia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return terapiaRepository.count();
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

    protected Terapia getPersistedTerapia(Terapia terapia) {
        return terapiaRepository.findById(terapia.getId()).orElseThrow();
    }

    protected void assertPersistedTerapiaToMatchAllProperties(Terapia expectedTerapia) {
        assertTerapiaAllPropertiesEquals(expectedTerapia, getPersistedTerapia(expectedTerapia));
    }

    protected void assertPersistedTerapiaToMatchUpdatableProperties(Terapia expectedTerapia) {
        assertTerapiaAllUpdatablePropertiesEquals(expectedTerapia, getPersistedTerapia(expectedTerapia));
    }
}
