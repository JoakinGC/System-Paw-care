package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.EstudiosAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Estudios;
import com.veterinary.sistema.repository.EstudiosRepository;
import com.veterinary.sistema.service.EstudiosService;
import com.veterinary.sistema.service.dto.EstudiosDTO;
import com.veterinary.sistema.service.mapper.EstudiosMapper;
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
 * Integration tests for the {@link EstudiosResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EstudiosResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_CURSADO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_CURSADO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOMBRE_INSITUTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_INSITUTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/estudios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EstudiosRepository estudiosRepository;

    @Mock
    private EstudiosRepository estudiosRepositoryMock;

    @Autowired
    private EstudiosMapper estudiosMapper;

    @Mock
    private EstudiosService estudiosServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEstudiosMockMvc;

    private Estudios estudios;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estudios createEntity(EntityManager em) {
        Estudios estudios = new Estudios()
            .nombre(DEFAULT_NOMBRE)
            .fechaCursado(DEFAULT_FECHA_CURSADO)
            .nombreInsituto(DEFAULT_NOMBRE_INSITUTO);
        return estudios;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Estudios createUpdatedEntity(EntityManager em) {
        Estudios estudios = new Estudios()
            .nombre(UPDATED_NOMBRE)
            .fechaCursado(UPDATED_FECHA_CURSADO)
            .nombreInsituto(UPDATED_NOMBRE_INSITUTO);
        return estudios;
    }

    @BeforeEach
    public void initTest() {
        estudios = createEntity(em);
    }

    @Test
    @Transactional
    void createEstudios() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Estudios
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);
        var returnedEstudiosDTO = om.readValue(
            restEstudiosMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estudiosDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EstudiosDTO.class
        );

        // Validate the Estudios in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEstudios = estudiosMapper.toEntity(returnedEstudiosDTO);
        assertEstudiosUpdatableFieldsEquals(returnedEstudios, getPersistedEstudios(returnedEstudios));
    }

    @Test
    @Transactional
    void createEstudiosWithExistingId() throws Exception {
        // Create the Estudios with an existing ID
        estudios.setId(1L);
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstudiosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estudiosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Estudios in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFechaCursadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        estudios.setFechaCursado(null);

        // Create the Estudios, which fails.
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);

        restEstudiosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estudiosDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);

        // Get all the estudiosList
        restEstudiosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estudios.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].fechaCursado").value(hasItem(DEFAULT_FECHA_CURSADO.toString())))
            .andExpect(jsonPath("$.[*].nombreInsituto").value(hasItem(DEFAULT_NOMBRE_INSITUTO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEstudiosWithEagerRelationshipsIsEnabled() throws Exception {
        when(estudiosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEstudiosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(estudiosServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEstudiosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(estudiosServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEstudiosMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(estudiosRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);

        // Get the estudios
        restEstudiosMockMvc
            .perform(get(ENTITY_API_URL_ID, estudios.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(estudios.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.fechaCursado").value(DEFAULT_FECHA_CURSADO.toString()))
            .andExpect(jsonPath("$.nombreInsituto").value(DEFAULT_NOMBRE_INSITUTO));
    }

    @Test
    @Transactional
    void getNonExistingEstudios() throws Exception {
        // Get the estudios
        restEstudiosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estudios
        Estudios updatedEstudios = estudiosRepository.findById(estudios.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEstudios are not directly saved in db
        em.detach(updatedEstudios);
        updatedEstudios.nombre(UPDATED_NOMBRE).fechaCursado(UPDATED_FECHA_CURSADO).nombreInsituto(UPDATED_NOMBRE_INSITUTO);
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(updatedEstudios);

        restEstudiosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estudiosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estudiosDTO))
            )
            .andExpect(status().isOk());

        // Validate the Estudios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEstudiosToMatchAllProperties(updatedEstudios);
    }

    @Test
    @Transactional
    void putNonExistingEstudios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estudios.setId(longCount.incrementAndGet());

        // Create the Estudios
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstudiosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, estudiosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estudiosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estudios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEstudios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estudios.setId(longCount.incrementAndGet());

        // Create the Estudios
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstudiosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(estudiosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estudios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEstudios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estudios.setId(longCount.incrementAndGet());

        // Create the Estudios
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstudiosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(estudiosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estudios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEstudiosWithPatch() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estudios using partial update
        Estudios partialUpdatedEstudios = new Estudios();
        partialUpdatedEstudios.setId(estudios.getId());

        partialUpdatedEstudios.nombreInsituto(UPDATED_NOMBRE_INSITUTO);

        restEstudiosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstudios.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstudios))
            )
            .andExpect(status().isOk());

        // Validate the Estudios in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstudiosUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEstudios, estudios), getPersistedEstudios(estudios));
    }

    @Test
    @Transactional
    void fullUpdateEstudiosWithPatch() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the estudios using partial update
        Estudios partialUpdatedEstudios = new Estudios();
        partialUpdatedEstudios.setId(estudios.getId());

        partialUpdatedEstudios.nombre(UPDATED_NOMBRE).fechaCursado(UPDATED_FECHA_CURSADO).nombreInsituto(UPDATED_NOMBRE_INSITUTO);

        restEstudiosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEstudios.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEstudios))
            )
            .andExpect(status().isOk());

        // Validate the Estudios in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEstudiosUpdatableFieldsEquals(partialUpdatedEstudios, getPersistedEstudios(partialUpdatedEstudios));
    }

    @Test
    @Transactional
    void patchNonExistingEstudios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estudios.setId(longCount.incrementAndGet());

        // Create the Estudios
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstudiosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, estudiosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estudiosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estudios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEstudios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estudios.setId(longCount.incrementAndGet());

        // Create the Estudios
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstudiosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(estudiosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Estudios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEstudios() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        estudios.setId(longCount.incrementAndGet());

        // Create the Estudios
        EstudiosDTO estudiosDTO = estudiosMapper.toDto(estudios);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEstudiosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(estudiosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Estudios in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEstudios() throws Exception {
        // Initialize the database
        estudiosRepository.saveAndFlush(estudios);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the estudios
        restEstudiosMockMvc
            .perform(delete(ENTITY_API_URL_ID, estudios.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return estudiosRepository.count();
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

    protected Estudios getPersistedEstudios(Estudios estudios) {
        return estudiosRepository.findById(estudios.getId()).orElseThrow();
    }

    protected void assertPersistedEstudiosToMatchAllProperties(Estudios expectedEstudios) {
        assertEstudiosAllPropertiesEquals(expectedEstudios, getPersistedEstudios(expectedEstudios));
    }

    protected void assertPersistedEstudiosToMatchUpdatableProperties(Estudios expectedEstudios) {
        assertEstudiosAllUpdatablePropertiesEquals(expectedEstudios, getPersistedEstudios(expectedEstudios));
    }
}
