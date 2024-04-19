package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.RazaAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Raza;
import com.veterinary.sistema.repository.RazaRepository;
import com.veterinary.sistema.service.dto.RazaDTO;
import com.veterinary.sistema.service.mapper.RazaMapper;
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
 * Integration tests for the {@link RazaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RazaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_CIENTIFICO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CIENTIFICO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/razas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RazaRepository razaRepository;

    @Autowired
    private RazaMapper razaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRazaMockMvc;

    private Raza raza;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raza createEntity(EntityManager em) {
        Raza raza = new Raza().nombre(DEFAULT_NOMBRE).nombreCientifico(DEFAULT_NOMBRE_CIENTIFICO);
        return raza;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raza createUpdatedEntity(EntityManager em) {
        Raza raza = new Raza().nombre(UPDATED_NOMBRE).nombreCientifico(UPDATED_NOMBRE_CIENTIFICO);
        return raza;
    }

    @BeforeEach
    public void initTest() {
        raza = createEntity(em);
    }

    @Test
    @Transactional
    void createRaza() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Raza
        RazaDTO razaDTO = razaMapper.toDto(raza);
        var returnedRazaDTO = om.readValue(
            restRazaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(razaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RazaDTO.class
        );

        // Validate the Raza in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRaza = razaMapper.toEntity(returnedRazaDTO);
        assertRazaUpdatableFieldsEquals(returnedRaza, getPersistedRaza(returnedRaza));
    }

    @Test
    @Transactional
    void createRazaWithExistingId() throws Exception {
        // Create the Raza with an existing ID
        raza.setId(1L);
        RazaDTO razaDTO = razaMapper.toDto(raza);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRazaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(razaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Raza in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRazas() throws Exception {
        // Initialize the database
        razaRepository.saveAndFlush(raza);

        // Get all the razaList
        restRazaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raza.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].nombreCientifico").value(hasItem(DEFAULT_NOMBRE_CIENTIFICO)));
    }

    @Test
    @Transactional
    void getRaza() throws Exception {
        // Initialize the database
        razaRepository.saveAndFlush(raza);

        // Get the raza
        restRazaMockMvc
            .perform(get(ENTITY_API_URL_ID, raza.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(raza.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.nombreCientifico").value(DEFAULT_NOMBRE_CIENTIFICO));
    }

    @Test
    @Transactional
    void getNonExistingRaza() throws Exception {
        // Get the raza
        restRazaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRaza() throws Exception {
        // Initialize the database
        razaRepository.saveAndFlush(raza);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raza
        Raza updatedRaza = razaRepository.findById(raza.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRaza are not directly saved in db
        em.detach(updatedRaza);
        updatedRaza.nombre(UPDATED_NOMBRE).nombreCientifico(UPDATED_NOMBRE_CIENTIFICO);
        RazaDTO razaDTO = razaMapper.toDto(updatedRaza);

        restRazaMockMvc
            .perform(put(ENTITY_API_URL_ID, razaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(razaDTO)))
            .andExpect(status().isOk());

        // Validate the Raza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRazaToMatchAllProperties(updatedRaza);
    }

    @Test
    @Transactional
    void putNonExistingRaza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raza.setId(longCount.incrementAndGet());

        // Create the Raza
        RazaDTO razaDTO = razaMapper.toDto(raza);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRazaMockMvc
            .perform(put(ENTITY_API_URL_ID, razaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(razaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Raza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRaza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raza.setId(longCount.incrementAndGet());

        // Create the Raza
        RazaDTO razaDTO = razaMapper.toDto(raza);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRazaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(razaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRaza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raza.setId(longCount.incrementAndGet());

        // Create the Raza
        RazaDTO razaDTO = razaMapper.toDto(raza);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRazaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(razaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Raza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRazaWithPatch() throws Exception {
        // Initialize the database
        razaRepository.saveAndFlush(raza);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raza using partial update
        Raza partialUpdatedRaza = new Raza();
        partialUpdatedRaza.setId(raza.getId());

        partialUpdatedRaza.nombreCientifico(UPDATED_NOMBRE_CIENTIFICO);

        restRazaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaza.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRaza))
            )
            .andExpect(status().isOk());

        // Validate the Raza in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRazaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRaza, raza), getPersistedRaza(raza));
    }

    @Test
    @Transactional
    void fullUpdateRazaWithPatch() throws Exception {
        // Initialize the database
        razaRepository.saveAndFlush(raza);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the raza using partial update
        Raza partialUpdatedRaza = new Raza();
        partialUpdatedRaza.setId(raza.getId());

        partialUpdatedRaza.nombre(UPDATED_NOMBRE).nombreCientifico(UPDATED_NOMBRE_CIENTIFICO);

        restRazaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaza.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRaza))
            )
            .andExpect(status().isOk());

        // Validate the Raza in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRazaUpdatableFieldsEquals(partialUpdatedRaza, getPersistedRaza(partialUpdatedRaza));
    }

    @Test
    @Transactional
    void patchNonExistingRaza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raza.setId(longCount.incrementAndGet());

        // Create the Raza
        RazaDTO razaDTO = razaMapper.toDto(raza);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRazaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, razaDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(razaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRaza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raza.setId(longCount.incrementAndGet());

        // Create the Raza
        RazaDTO razaDTO = razaMapper.toDto(raza);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRazaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(razaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Raza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRaza() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        raza.setId(longCount.incrementAndGet());

        // Create the Raza
        RazaDTO razaDTO = razaMapper.toDto(raza);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRazaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(razaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Raza in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRaza() throws Exception {
        // Initialize the database
        razaRepository.saveAndFlush(raza);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the raza
        restRazaMockMvc
            .perform(delete(ENTITY_API_URL_ID, raza.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return razaRepository.count();
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

    protected Raza getPersistedRaza(Raza raza) {
        return razaRepository.findById(raza.getId()).orElseThrow();
    }

    protected void assertPersistedRazaToMatchAllProperties(Raza expectedRaza) {
        assertRazaAllPropertiesEquals(expectedRaza, getPersistedRaza(expectedRaza));
    }

    protected void assertPersistedRazaToMatchUpdatableProperties(Raza expectedRaza) {
        assertRazaAllUpdatablePropertiesEquals(expectedRaza, getPersistedRaza(expectedRaza));
    }
}
