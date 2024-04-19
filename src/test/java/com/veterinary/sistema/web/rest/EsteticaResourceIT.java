package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.EsteticaAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Estetica;
import com.veterinary.sistema.repository.EsteticaRepository;
import com.veterinary.sistema.service.dto.EsteticaDTO;
import com.veterinary.sistema.service.mapper.EsteticaMapper;
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
 * Integration tests for the {@link EsteticaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EsteticaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/esteticas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EsteticaRepository esteticaRepository;

    @Autowired
    private EsteticaMapper esteticaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEsteticaMockMvc;

    private Estetica estetica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estetica createEntity(EntityManager em) {
        Estetica estetica = new Estetica().nombre(DEFAULT_NOMBRE).direcion(DEFAULT_DIRECION).telefono(DEFAULT_TELEFONO);
        return estetica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estetica createUpdatedEntity(EntityManager em) {
        Estetica estetica = new Estetica().nombre(UPDATED_NOMBRE).direcion(UPDATED_DIRECION).telefono(UPDATED_TELEFONO);
        return estetica;
    }

    @BeforeEach
    public void initTest() {
        estetica = createEntity(em);
    }

    @Test
    @Transactional
    void createEstetica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Estetica
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(estetica);
        var returnedEsteticaDTO = om.readValue(
            restEsteticaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(esteticaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EsteticaDTO.class
        );

        // Validate the Estetica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEstetica = esteticaMapper.toEntity(returnedEsteticaDTO);
        assertEsteticaUpdatableFieldsEquals(returnedEstetica, getPersistedEstetica(returnedEstetica));
    }

    @Test
    @Transactional
    void createEsteticaWithExistingId() throws Exception {
        // Create the Estetica with an existing ID
        estetica.setId(1L);
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(estetica);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEsteticaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(esteticaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Estetica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEsteticas() throws Exception {
        // Initialize the database
        esteticaRepository.saveAndFlush(estetica);

        // Get all the esteticaList
        restEsteticaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estetica.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direcion").value(hasItem(DEFAULT_DIRECION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @Test
    @Transactional
    void getEstetica() throws Exception {
        // Initialize the database
        esteticaRepository.saveAndFlush(estetica);

        // Get the estetica
        restEsteticaMockMvc
            .perform(get(ENTITY_API_URL_ID, estetica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estetica.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direcion").value(DEFAULT_DIRECION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingEstetica() throws Exception {
        // Get the estetica
        restEsteticaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstetica() throws Exception {
        // Initialize the database
        esteticaRepository.saveAndFlush(estetica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estetica
        Estetica updatedEstetica = esteticaRepository.findById(estetica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstetica are not directly saved in db
        em.detach(updatedEstetica);
        updatedEstetica.nombre(UPDATED_NOMBRE).direcion(UPDATED_DIRECION).telefono(UPDATED_TELEFONO);
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(updatedEstetica);

        restEsteticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, esteticaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(esteticaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Estetica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEsteticaToMatchAllProperties(updatedEstetica);
    }

    @Test
    @Transactional
    void putNonExistingEstetica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estetica.setId(longCount.incrementAndGet());

        // Create the Estetica
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(estetica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsteticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, esteticaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(esteticaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estetica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstetica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estetica.setId(longCount.incrementAndGet());

        // Create the Estetica
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(estetica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsteticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(esteticaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estetica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstetica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estetica.setId(longCount.incrementAndGet());

        // Create the Estetica
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(estetica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsteticaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(esteticaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estetica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEsteticaWithPatch() throws Exception {
        // Initialize the database
        esteticaRepository.saveAndFlush(estetica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estetica using partial update
        Estetica partialUpdatedEstetica = new Estetica();
        partialUpdatedEstetica.setId(estetica.getId());

        partialUpdatedEstetica.telefono(UPDATED_TELEFONO);

        restEsteticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstetica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstetica))
            )
            .andExpect(status().isOk());

        // Validate the Estetica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEsteticaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEstetica, estetica), getPersistedEstetica(estetica));
    }

    @Test
    @Transactional
    void fullUpdateEsteticaWithPatch() throws Exception {
        // Initialize the database
        esteticaRepository.saveAndFlush(estetica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estetica using partial update
        Estetica partialUpdatedEstetica = new Estetica();
        partialUpdatedEstetica.setId(estetica.getId());

        partialUpdatedEstetica.nombre(UPDATED_NOMBRE).direcion(UPDATED_DIRECION).telefono(UPDATED_TELEFONO);

        restEsteticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstetica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstetica))
            )
            .andExpect(status().isOk());

        // Validate the Estetica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEsteticaUpdatableFieldsEquals(partialUpdatedEstetica, getPersistedEstetica(partialUpdatedEstetica));
    }

    @Test
    @Transactional
    void patchNonExistingEstetica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estetica.setId(longCount.incrementAndGet());

        // Create the Estetica
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(estetica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsteticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, esteticaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(esteticaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estetica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstetica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estetica.setId(longCount.incrementAndGet());

        // Create the Estetica
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(estetica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsteticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(esteticaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estetica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstetica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estetica.setId(longCount.incrementAndGet());

        // Create the Estetica
        EsteticaDTO esteticaDTO = esteticaMapper.toDto(estetica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsteticaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(esteticaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estetica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstetica() throws Exception {
        // Initialize the database
        esteticaRepository.saveAndFlush(estetica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the estetica
        restEsteticaMockMvc
            .perform(delete(ENTITY_API_URL_ID, estetica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return esteticaRepository.count();
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

    protected Estetica getPersistedEstetica(Estetica estetica) {
        return esteticaRepository.findById(estetica.getId()).orElseThrow();
    }

    protected void assertPersistedEsteticaToMatchAllProperties(Estetica expectedEstetica) {
        assertEsteticaAllPropertiesEquals(expectedEstetica, getPersistedEstetica(expectedEstetica));
    }

    protected void assertPersistedEsteticaToMatchUpdatableProperties(Estetica expectedEstetica) {
        assertEsteticaAllUpdatablePropertiesEquals(expectedEstetica, getPersistedEstetica(expectedEstetica));
    }
}
