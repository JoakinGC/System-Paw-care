package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.FactoresAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Factores;
import com.veterinary.sistema.repository.FactoresRepository;
import com.veterinary.sistema.service.dto.FactoresDTO;
import com.veterinary.sistema.service.mapper.FactoresMapper;
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
 * Integration tests for the {@link FactoresResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactoresResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/factores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactoresRepository factoresRepository;

    @Autowired
    private FactoresMapper factoresMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactoresMockMvc;

    private Factores factores;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factores createEntity(EntityManager em) {
        Factores factores = new Factores().nombre(DEFAULT_NOMBRE).tipo(DEFAULT_TIPO).descripcion(DEFAULT_DESCRIPCION);
        return factores;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factores createUpdatedEntity(EntityManager em) {
        Factores factores = new Factores().nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).descripcion(UPDATED_DESCRIPCION);
        return factores;
    }

    @BeforeEach
    public void initTest() {
        factores = createEntity(em);
    }

    @Test
    @Transactional
    void createFactores() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Factores
        FactoresDTO factoresDTO = factoresMapper.toDto(factores);
        var returnedFactoresDTO = om.readValue(
            restFactoresMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factoresDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactoresDTO.class
        );

        // Validate the Factores in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFactores = factoresMapper.toEntity(returnedFactoresDTO);
        assertFactoresUpdatableFieldsEquals(returnedFactores, getPersistedFactores(returnedFactores));
    }

    @Test
    @Transactional
    void createFactoresWithExistingId() throws Exception {
        // Create the Factores with an existing ID
        factores.setId(1L);
        FactoresDTO factoresDTO = factoresMapper.toDto(factores);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactoresMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factoresDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factores in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFactores() throws Exception {
        // Initialize the database
        factoresRepository.saveAndFlush(factores);

        // Get all the factoresList
        restFactoresMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factores.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getFactores() throws Exception {
        // Initialize the database
        factoresRepository.saveAndFlush(factores);

        // Get the factores
        restFactoresMockMvc
            .perform(get(ENTITY_API_URL_ID, factores.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factores.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingFactores() throws Exception {
        // Get the factores
        restFactoresMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFactores() throws Exception {
        // Initialize the database
        factoresRepository.saveAndFlush(factores);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factores
        Factores updatedFactores = factoresRepository.findById(factores.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFactores are not directly saved in db
        em.detach(updatedFactores);
        updatedFactores.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).descripcion(UPDATED_DESCRIPCION);
        FactoresDTO factoresDTO = factoresMapper.toDto(updatedFactores);

        restFactoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factoresDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factoresDTO))
            )
            .andExpect(status().isOk());

        // Validate the Factores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactoresToMatchAllProperties(updatedFactores);
    }

    @Test
    @Transactional
    void putNonExistingFactores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factores.setId(longCount.incrementAndGet());

        // Create the Factores
        FactoresDTO factoresDTO = factoresMapper.toDto(factores);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factoresDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factoresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factores.setId(longCount.incrementAndGet());

        // Create the Factores
        FactoresDTO factoresDTO = factoresMapper.toDto(factores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactoresMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factoresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factores.setId(longCount.incrementAndGet());

        // Create the Factores
        FactoresDTO factoresDTO = factoresMapper.toDto(factores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactoresMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factoresDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactoresWithPatch() throws Exception {
        // Initialize the database
        factoresRepository.saveAndFlush(factores);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factores using partial update
        Factores partialUpdatedFactores = new Factores();
        partialUpdatedFactores.setId(factores.getId());

        partialUpdatedFactores.descripcion(UPDATED_DESCRIPCION);

        restFactoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactores.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactores))
            )
            .andExpect(status().isOk());

        // Validate the Factores in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactoresUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFactores, factores), getPersistedFactores(factores));
    }

    @Test
    @Transactional
    void fullUpdateFactoresWithPatch() throws Exception {
        // Initialize the database
        factoresRepository.saveAndFlush(factores);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factores using partial update
        Factores partialUpdatedFactores = new Factores();
        partialUpdatedFactores.setId(factores.getId());

        partialUpdatedFactores.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).descripcion(UPDATED_DESCRIPCION);

        restFactoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactores.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactores))
            )
            .andExpect(status().isOk());

        // Validate the Factores in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactoresUpdatableFieldsEquals(partialUpdatedFactores, getPersistedFactores(partialUpdatedFactores));
    }

    @Test
    @Transactional
    void patchNonExistingFactores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factores.setId(longCount.incrementAndGet());

        // Create the Factores
        FactoresDTO factoresDTO = factoresMapper.toDto(factores);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factoresDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factoresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factores.setId(longCount.incrementAndGet());

        // Create the Factores
        FactoresDTO factoresDTO = factoresMapper.toDto(factores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactoresMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factoresDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactores() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factores.setId(longCount.incrementAndGet());

        // Create the Factores
        FactoresDTO factoresDTO = factoresMapper.toDto(factores);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactoresMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factoresDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factores in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactores() throws Exception {
        // Initialize the database
        factoresRepository.saveAndFlush(factores);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the factores
        restFactoresMockMvc
            .perform(delete(ENTITY_API_URL_ID, factores.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return factoresRepository.count();
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

    protected Factores getPersistedFactores(Factores factores) {
        return factoresRepository.findById(factores.getId()).orElseThrow();
    }

    protected void assertPersistedFactoresToMatchAllProperties(Factores expectedFactores) {
        assertFactoresAllPropertiesEquals(expectedFactores, getPersistedFactores(expectedFactores));
    }

    protected void assertPersistedFactoresToMatchUpdatableProperties(Factores expectedFactores) {
        assertFactoresAllUpdatablePropertiesEquals(expectedFactores, getPersistedFactores(expectedFactores));
    }
}
