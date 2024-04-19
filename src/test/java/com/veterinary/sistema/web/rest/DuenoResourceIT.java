package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.DuenoAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Dueno;
import com.veterinary.sistema.repository.DuenoRepository;
import com.veterinary.sistema.service.dto.DuenoDTO;
import com.veterinary.sistema.service.mapper.DuenoMapper;
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
 * Integration tests for the {@link DuenoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DuenoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/duenos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private DuenoMapper duenoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDuenoMockMvc;

    private Dueno dueno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dueno createEntity(EntityManager em) {
        Dueno dueno = new Dueno().nombre(DEFAULT_NOMBRE).apellido(DEFAULT_APELLIDO).direccion(DEFAULT_DIRECCION).telefono(DEFAULT_TELEFONO);
        return dueno;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dueno createUpdatedEntity(EntityManager em) {
        Dueno dueno = new Dueno().nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).direccion(UPDATED_DIRECCION).telefono(UPDATED_TELEFONO);
        return dueno;
    }

    @BeforeEach
    public void initTest() {
        dueno = createEntity(em);
    }

    @Test
    @Transactional
    void createDueno() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dueno
        DuenoDTO duenoDTO = duenoMapper.toDto(dueno);
        var returnedDuenoDTO = om.readValue(
            restDuenoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(duenoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DuenoDTO.class
        );

        // Validate the Dueno in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDueno = duenoMapper.toEntity(returnedDuenoDTO);
        assertDuenoUpdatableFieldsEquals(returnedDueno, getPersistedDueno(returnedDueno));
    }

    @Test
    @Transactional
    void createDuenoWithExistingId() throws Exception {
        // Create the Dueno with an existing ID
        dueno.setId(1L);
        DuenoDTO duenoDTO = duenoMapper.toDto(dueno);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDuenoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(duenoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dueno in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDuenos() throws Exception {
        // Initialize the database
        duenoRepository.saveAndFlush(dueno);

        // Get all the duenoList
        restDuenoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dueno.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)));
    }

    @Test
    @Transactional
    void getDueno() throws Exception {
        // Initialize the database
        duenoRepository.saveAndFlush(dueno);

        // Get the dueno
        restDuenoMockMvc
            .perform(get(ENTITY_API_URL_ID, dueno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dueno.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO));
    }

    @Test
    @Transactional
    void getNonExistingDueno() throws Exception {
        // Get the dueno
        restDuenoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDueno() throws Exception {
        // Initialize the database
        duenoRepository.saveAndFlush(dueno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dueno
        Dueno updatedDueno = duenoRepository.findById(dueno.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDueno are not directly saved in db
        em.detach(updatedDueno);
        updatedDueno.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).direccion(UPDATED_DIRECCION).telefono(UPDATED_TELEFONO);
        DuenoDTO duenoDTO = duenoMapper.toDto(updatedDueno);

        restDuenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, duenoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(duenoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dueno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDuenoToMatchAllProperties(updatedDueno);
    }

    @Test
    @Transactional
    void putNonExistingDueno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dueno.setId(longCount.incrementAndGet());

        // Create the Dueno
        DuenoDTO duenoDTO = duenoMapper.toDto(dueno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDuenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, duenoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(duenoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dueno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDueno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dueno.setId(longCount.incrementAndGet());

        // Create the Dueno
        DuenoDTO duenoDTO = duenoMapper.toDto(dueno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDuenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(duenoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dueno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDueno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dueno.setId(longCount.incrementAndGet());

        // Create the Dueno
        DuenoDTO duenoDTO = duenoMapper.toDto(dueno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDuenoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(duenoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dueno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDuenoWithPatch() throws Exception {
        // Initialize the database
        duenoRepository.saveAndFlush(dueno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dueno using partial update
        Dueno partialUpdatedDueno = new Dueno();
        partialUpdatedDueno.setId(dueno.getId());

        partialUpdatedDueno.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).direccion(UPDATED_DIRECCION).telefono(UPDATED_TELEFONO);

        restDuenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDueno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDueno))
            )
            .andExpect(status().isOk());

        // Validate the Dueno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDuenoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDueno, dueno), getPersistedDueno(dueno));
    }

    @Test
    @Transactional
    void fullUpdateDuenoWithPatch() throws Exception {
        // Initialize the database
        duenoRepository.saveAndFlush(dueno);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dueno using partial update
        Dueno partialUpdatedDueno = new Dueno();
        partialUpdatedDueno.setId(dueno.getId());

        partialUpdatedDueno.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).direccion(UPDATED_DIRECCION).telefono(UPDATED_TELEFONO);

        restDuenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDueno.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDueno))
            )
            .andExpect(status().isOk());

        // Validate the Dueno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDuenoUpdatableFieldsEquals(partialUpdatedDueno, getPersistedDueno(partialUpdatedDueno));
    }

    @Test
    @Transactional
    void patchNonExistingDueno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dueno.setId(longCount.incrementAndGet());

        // Create the Dueno
        DuenoDTO duenoDTO = duenoMapper.toDto(dueno);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDuenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, duenoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(duenoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dueno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDueno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dueno.setId(longCount.incrementAndGet());

        // Create the Dueno
        DuenoDTO duenoDTO = duenoMapper.toDto(dueno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDuenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(duenoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dueno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDueno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dueno.setId(longCount.incrementAndGet());

        // Create the Dueno
        DuenoDTO duenoDTO = duenoMapper.toDto(dueno);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDuenoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(duenoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dueno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDueno() throws Exception {
        // Initialize the database
        duenoRepository.saveAndFlush(dueno);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dueno
        restDuenoMockMvc
            .perform(delete(ENTITY_API_URL_ID, dueno.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return duenoRepository.count();
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

    protected Dueno getPersistedDueno(Dueno dueno) {
        return duenoRepository.findById(dueno.getId()).orElseThrow();
    }

    protected void assertPersistedDuenoToMatchAllProperties(Dueno expectedDueno) {
        assertDuenoAllPropertiesEquals(expectedDueno, getPersistedDueno(expectedDueno));
    }

    protected void assertPersistedDuenoToMatchUpdatableProperties(Dueno expectedDueno) {
        assertDuenoAllUpdatablePropertiesEquals(expectedDueno, getPersistedDueno(expectedDueno));
    }
}
