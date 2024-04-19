package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.EspecieAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Especie;
import com.veterinary.sistema.repository.EspecieRepository;
import com.veterinary.sistema.service.dto.EspecieDTO;
import com.veterinary.sistema.service.mapper.EspecieMapper;
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
 * Integration tests for the {@link EspecieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EspecieResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_CIENTIFICO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CIENTIFICO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/especies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EspecieRepository especieRepository;

    @Autowired
    private EspecieMapper especieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEspecieMockMvc;

    private Especie especie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especie createEntity(EntityManager em) {
        Especie especie = new Especie().nombre(DEFAULT_NOMBRE).nombreCientifico(DEFAULT_NOMBRE_CIENTIFICO);
        return especie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especie createUpdatedEntity(EntityManager em) {
        Especie especie = new Especie().nombre(UPDATED_NOMBRE).nombreCientifico(UPDATED_NOMBRE_CIENTIFICO);
        return especie;
    }

    @BeforeEach
    public void initTest() {
        especie = createEntity(em);
    }

    @Test
    @Transactional
    void createEspecie() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Especie
        EspecieDTO especieDTO = especieMapper.toDto(especie);
        var returnedEspecieDTO = om.readValue(
            restEspecieMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especieDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EspecieDTO.class
        );

        // Validate the Especie in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEspecie = especieMapper.toEntity(returnedEspecieDTO);
        assertEspecieUpdatableFieldsEquals(returnedEspecie, getPersistedEspecie(returnedEspecie));
    }

    @Test
    @Transactional
    void createEspecieWithExistingId() throws Exception {
        // Create the Especie with an existing ID
        especie.setId(1L);
        EspecieDTO especieDTO = especieMapper.toDto(especie);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Especie in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEspecies() throws Exception {
        // Initialize the database
        especieRepository.saveAndFlush(especie);

        // Get all the especieList
        restEspecieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especie.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].nombreCientifico").value(hasItem(DEFAULT_NOMBRE_CIENTIFICO)));
    }

    @Test
    @Transactional
    void getEspecie() throws Exception {
        // Initialize the database
        especieRepository.saveAndFlush(especie);

        // Get the especie
        restEspecieMockMvc
            .perform(get(ENTITY_API_URL_ID, especie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(especie.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.nombreCientifico").value(DEFAULT_NOMBRE_CIENTIFICO));
    }

    @Test
    @Transactional
    void getNonExistingEspecie() throws Exception {
        // Get the especie
        restEspecieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEspecie() throws Exception {
        // Initialize the database
        especieRepository.saveAndFlush(especie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especie
        Especie updatedEspecie = especieRepository.findById(especie.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEspecie are not directly saved in db
        em.detach(updatedEspecie);
        updatedEspecie.nombre(UPDATED_NOMBRE).nombreCientifico(UPDATED_NOMBRE_CIENTIFICO);
        EspecieDTO especieDTO = especieMapper.toDto(updatedEspecie);

        restEspecieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, especieDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Especie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEspecieToMatchAllProperties(updatedEspecie);
    }

    @Test
    @Transactional
    void putNonExistingEspecie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especie.setId(longCount.incrementAndGet());

        // Create the Especie
        EspecieDTO especieDTO = especieMapper.toDto(especie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, especieDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEspecie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especie.setId(longCount.incrementAndGet());

        // Create the Especie
        EspecieDTO especieDTO = especieMapper.toDto(especie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(especieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEspecie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especie.setId(longCount.incrementAndGet());

        // Create the Especie
        EspecieDTO especieDTO = especieMapper.toDto(especie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEspecieWithPatch() throws Exception {
        // Initialize the database
        especieRepository.saveAndFlush(especie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especie using partial update
        Especie partialUpdatedEspecie = new Especie();
        partialUpdatedEspecie.setId(especie.getId());

        partialUpdatedEspecie.nombre(UPDATED_NOMBRE);

        restEspecieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecie.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEspecie))
            )
            .andExpect(status().isOk());

        // Validate the Especie in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEspecieUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEspecie, especie), getPersistedEspecie(especie));
    }

    @Test
    @Transactional
    void fullUpdateEspecieWithPatch() throws Exception {
        // Initialize the database
        especieRepository.saveAndFlush(especie);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especie using partial update
        Especie partialUpdatedEspecie = new Especie();
        partialUpdatedEspecie.setId(especie.getId());

        partialUpdatedEspecie.nombre(UPDATED_NOMBRE).nombreCientifico(UPDATED_NOMBRE_CIENTIFICO);

        restEspecieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecie.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEspecie))
            )
            .andExpect(status().isOk());

        // Validate the Especie in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEspecieUpdatableFieldsEquals(partialUpdatedEspecie, getPersistedEspecie(partialUpdatedEspecie));
    }

    @Test
    @Transactional
    void patchNonExistingEspecie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especie.setId(longCount.incrementAndGet());

        // Create the Especie
        EspecieDTO especieDTO = especieMapper.toDto(especie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, especieDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(especieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEspecie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especie.setId(longCount.incrementAndGet());

        // Create the Especie
        EspecieDTO especieDTO = especieMapper.toDto(especie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(especieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEspecie() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especie.setId(longCount.incrementAndGet());

        // Create the Especie
        EspecieDTO especieDTO = especieMapper.toDto(especie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecieMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(especieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especie in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEspecie() throws Exception {
        // Initialize the database
        especieRepository.saveAndFlush(especie);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the especie
        restEspecieMockMvc
            .perform(delete(ENTITY_API_URL_ID, especie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return especieRepository.count();
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

    protected Especie getPersistedEspecie(Especie especie) {
        return especieRepository.findById(especie.getId()).orElseThrow();
    }

    protected void assertPersistedEspecieToMatchAllProperties(Especie expectedEspecie) {
        assertEspecieAllPropertiesEquals(expectedEspecie, getPersistedEspecie(expectedEspecie));
    }

    protected void assertPersistedEspecieToMatchUpdatableProperties(Especie expectedEspecie) {
        assertEspecieAllUpdatablePropertiesEquals(expectedEspecie, getPersistedEspecie(expectedEspecie));
    }
}
