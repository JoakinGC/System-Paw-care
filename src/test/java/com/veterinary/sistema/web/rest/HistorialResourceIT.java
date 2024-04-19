package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.HistorialAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Historial;
import com.veterinary.sistema.repository.HistorialRepository;
import com.veterinary.sistema.service.HistorialService;
import com.veterinary.sistema.service.dto.HistorialDTO;
import com.veterinary.sistema.service.mapper.HistorialMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HistorialResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HistorialResourceIT {

    private static final LocalDate DEFAULT_FECHA_CONSULTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CONSULTA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DIAGNOSTICO = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTICO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistorialRepository historialRepository;

    @Mock
    private HistorialRepository historialRepositoryMock;

    @Autowired
    private HistorialMapper historialMapper;

    @Mock
    private HistorialService historialServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistorialMockMvc;

    private Historial historial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historial createEntity(EntityManager em) {
        Historial historial = new Historial().fechaConsulta(DEFAULT_FECHA_CONSULTA).diagnostico(DEFAULT_DIAGNOSTICO);
        return historial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historial createUpdatedEntity(EntityManager em) {
        Historial historial = new Historial().fechaConsulta(UPDATED_FECHA_CONSULTA).diagnostico(UPDATED_DIAGNOSTICO);
        return historial;
    }

    @BeforeEach
    public void initTest() {
        historial = createEntity(em);
    }

    @Test
    @Transactional
    void createHistorial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);
        var returnedHistorialDTO = om.readValue(
            restHistorialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historialDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistorialDTO.class
        );

        // Validate the Historial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistorial = historialMapper.toEntity(returnedHistorialDTO);
        assertHistorialUpdatableFieldsEquals(returnedHistorial, getPersistedHistorial(returnedHistorial));
    }

    @Test
    @Transactional
    void createHistorialWithExistingId() throws Exception {
        // Create the Historial with an existing ID
        historial.setId(1L);
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaConsultaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historial.setFechaConsulta(null);

        // Create the Historial, which fails.
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        restHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiagnosticoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historial.setDiagnostico(null);

        // Create the Historial, which fails.
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        restHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistorials() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        // Get all the historialList
        restHistorialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historial.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaConsulta").value(hasItem(DEFAULT_FECHA_CONSULTA.toString())))
            .andExpect(jsonPath("$.[*].diagnostico").value(hasItem(DEFAULT_DIAGNOSTICO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistorialsWithEagerRelationshipsIsEnabled() throws Exception {
        when(historialServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistorialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(historialServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistorialsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(historialServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistorialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(historialRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        // Get the historial
        restHistorialMockMvc
            .perform(get(ENTITY_API_URL_ID, historial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historial.getId().intValue()))
            .andExpect(jsonPath("$.fechaConsulta").value(DEFAULT_FECHA_CONSULTA.toString()))
            .andExpect(jsonPath("$.diagnostico").value(DEFAULT_DIAGNOSTICO));
    }

    @Test
    @Transactional
    void getNonExistingHistorial() throws Exception {
        // Get the historial
        restHistorialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historial
        Historial updatedHistorial = historialRepository.findById(historial.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistorial are not directly saved in db
        em.detach(updatedHistorial);
        updatedHistorial.fechaConsulta(UPDATED_FECHA_CONSULTA).diagnostico(UPDATED_DIAGNOSTICO);
        HistorialDTO historialDTO = historialMapper.toDto(updatedHistorial);

        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historialDTO))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistorialToMatchAllProperties(updatedHistorial);
    }

    @Test
    @Transactional
    void putNonExistingHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistorialWithPatch() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historial using partial update
        Historial partialUpdatedHistorial = new Historial();
        partialUpdatedHistorial.setId(historial.getId());

        partialUpdatedHistorial.fechaConsulta(UPDATED_FECHA_CONSULTA).diagnostico(UPDATED_DIAGNOSTICO);

        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistorial))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistorialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistorial, historial),
            getPersistedHistorial(historial)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistorialWithPatch() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historial using partial update
        Historial partialUpdatedHistorial = new Historial();
        partialUpdatedHistorial.setId(historial.getId());

        partialUpdatedHistorial.fechaConsulta(UPDATED_FECHA_CONSULTA).diagnostico(UPDATED_DIAGNOSTICO);

        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistorial))
            )
            .andExpect(status().isOk());

        // Validate the Historial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistorialUpdatableFieldsEquals(partialUpdatedHistorial, getPersistedHistorial(partialUpdatedHistorial));
    }

    @Test
    @Transactional
    void patchNonExistingHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historial.setId(longCount.incrementAndGet());

        // Create the Historial
        HistorialDTO historialDTO = historialMapper.toDto(historial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistorialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistorial() throws Exception {
        // Initialize the database
        historialRepository.saveAndFlush(historial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historial
        restHistorialMockMvc
            .perform(delete(ENTITY_API_URL_ID, historial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historialRepository.count();
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

    protected Historial getPersistedHistorial(Historial historial) {
        return historialRepository.findById(historial.getId()).orElseThrow();
    }

    protected void assertPersistedHistorialToMatchAllProperties(Historial expectedHistorial) {
        assertHistorialAllPropertiesEquals(expectedHistorial, getPersistedHistorial(expectedHistorial));
    }

    protected void assertPersistedHistorialToMatchUpdatableProperties(Historial expectedHistorial) {
        assertHistorialAllUpdatablePropertiesEquals(expectedHistorial, getPersistedHistorial(expectedHistorial));
    }
}
