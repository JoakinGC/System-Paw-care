package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.MedicamentoAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Medicamento;
import com.veterinary.sistema.repository.MedicamentoRepository;
import com.veterinary.sistema.service.dto.MedicamentoDTO;
import com.veterinary.sistema.service.mapper.MedicamentoMapper;
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
 * Integration tests for the {@link MedicamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicamentoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private MedicamentoMapper medicamentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicamentoMockMvc;

    private Medicamento medicamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicamento createEntity(EntityManager em) {
        Medicamento medicamento = new Medicamento().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return medicamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicamento createUpdatedEntity(EntityManager em) {
        Medicamento medicamento = new Medicamento().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return medicamento;
    }

    @BeforeEach
    public void initTest() {
        medicamento = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicamento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);
        var returnedMedicamentoDTO = om.readValue(
            restMedicamentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedicamentoDTO.class
        );

        // Validate the Medicamento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedicamento = medicamentoMapper.toEntity(returnedMedicamentoDTO);
        assertMedicamentoUpdatableFieldsEquals(returnedMedicamento, getPersistedMedicamento(returnedMedicamento));
    }

    @Test
    @Transactional
    void createMedicamentoWithExistingId() throws Exception {
        // Create the Medicamento with an existing ID
        medicamento.setId(1L);
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medicamento.setNombre(null);

        // Create the Medicamento, which fails.
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        restMedicamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicamentos() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList
        restMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getMedicamento() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get the medicamento
        restMedicamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, medicamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicamento.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingMedicamento() throws Exception {
        // Get the medicamento
        restMedicamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedicamento() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicamento
        Medicamento updatedMedicamento = medicamentoRepository.findById(medicamento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedicamento are not directly saved in db
        em.detach(updatedMedicamento);
        updatedMedicamento.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(updatedMedicamento);

        restMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicamentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedicamentoToMatchAllProperties(updatedMedicamento);
    }

    @Test
    @Transactional
    void putNonExistingMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medicamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medicamentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicamentoWithPatch() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicamento using partial update
        Medicamento partialUpdatedMedicamento = new Medicamento();
        partialUpdatedMedicamento.setId(medicamento.getId());

        partialUpdatedMedicamento.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicamento))
            )
            .andExpect(status().isOk());

        // Validate the Medicamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicamentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedicamento, medicamento),
            getPersistedMedicamento(medicamento)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedicamentoWithPatch() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medicamento using partial update
        Medicamento partialUpdatedMedicamento = new Medicamento();
        partialUpdatedMedicamento.setId(medicamento.getId());

        partialUpdatedMedicamento.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedicamento))
            )
            .andExpect(status().isOk());

        // Validate the Medicamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedicamentoUpdatableFieldsEquals(partialUpdatedMedicamento, getPersistedMedicamento(partialUpdatedMedicamento));
    }

    @Test
    @Transactional
    void patchNonExistingMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicamentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medicamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medicamento.setId(longCount.incrementAndGet());

        // Create the Medicamento
        MedicamentoDTO medicamentoDTO = medicamentoMapper.toDto(medicamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medicamentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicamento() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medicamento
        restMedicamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medicamentoRepository.count();
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

    protected Medicamento getPersistedMedicamento(Medicamento medicamento) {
        return medicamentoRepository.findById(medicamento.getId()).orElseThrow();
    }

    protected void assertPersistedMedicamentoToMatchAllProperties(Medicamento expectedMedicamento) {
        assertMedicamentoAllPropertiesEquals(expectedMedicamento, getPersistedMedicamento(expectedMedicamento));
    }

    protected void assertPersistedMedicamentoToMatchUpdatableProperties(Medicamento expectedMedicamento) {
        assertMedicamentoAllUpdatablePropertiesEquals(expectedMedicamento, getPersistedMedicamento(expectedMedicamento));
    }
}
