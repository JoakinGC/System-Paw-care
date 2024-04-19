package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.EnfermedadAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Enfermedad;
import com.veterinary.sistema.repository.EnfermedadRepository;
import com.veterinary.sistema.service.EnfermedadService;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
import com.veterinary.sistema.service.mapper.EnfermedadMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link EnfermedadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EnfermedadResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enfermedads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnfermedadRepository enfermedadRepository;

    @Mock
    private EnfermedadRepository enfermedadRepositoryMock;

    @Autowired
    private EnfermedadMapper enfermedadMapper;

    @Mock
    private EnfermedadService enfermedadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnfermedadMockMvc;

    private Enfermedad enfermedad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermedad createEntity(EntityManager em) {
        Enfermedad enfermedad = new Enfermedad().nombre(DEFAULT_NOMBRE).descripcion(DEFAULT_DESCRIPCION);
        return enfermedad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enfermedad createUpdatedEntity(EntityManager em) {
        Enfermedad enfermedad = new Enfermedad().nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        return enfermedad;
    }

    @BeforeEach
    public void initTest() {
        enfermedad = createEntity(em);
    }

    @Test
    @Transactional
    void createEnfermedad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);
        var returnedEnfermedadDTO = om.readValue(
            restEnfermedadMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermedadDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnfermedadDTO.class
        );

        // Validate the Enfermedad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnfermedad = enfermedadMapper.toEntity(returnedEnfermedadDTO);
        assertEnfermedadUpdatableFieldsEquals(returnedEnfermedad, getPersistedEnfermedad(returnedEnfermedad));
    }

    @Test
    @Transactional
    void createEnfermedadWithExistingId() throws Exception {
        // Create the Enfermedad with an existing ID
        enfermedad.setId(1L);
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnfermedadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermedadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnfermedads() throws Exception {
        // Initialize the database
        enfermedadRepository.saveAndFlush(enfermedad);

        // Get all the enfermedadList
        restEnfermedadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfermedad.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEnfermedadsWithEagerRelationshipsIsEnabled() throws Exception {
        when(enfermedadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEnfermedadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(enfermedadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEnfermedadsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(enfermedadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEnfermedadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(enfermedadRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEnfermedad() throws Exception {
        // Initialize the database
        enfermedadRepository.saveAndFlush(enfermedad);

        // Get the enfermedad
        restEnfermedadMockMvc
            .perform(get(ENTITY_API_URL_ID, enfermedad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enfermedad.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingEnfermedad() throws Exception {
        // Get the enfermedad
        restEnfermedadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnfermedad() throws Exception {
        // Initialize the database
        enfermedadRepository.saveAndFlush(enfermedad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad
        Enfermedad updatedEnfermedad = enfermedadRepository.findById(enfermedad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnfermedad are not directly saved in db
        em.detach(updatedEnfermedad);
        updatedEnfermedad.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(updatedEnfermedad);

        restEnfermedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfermedadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnfermedadToMatchAllProperties(updatedEnfermedad);
    }

    @Test
    @Transactional
    void putNonExistingEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enfermedadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enfermedadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnfermedadWithPatch() throws Exception {
        // Initialize the database
        enfermedadRepository.saveAndFlush(enfermedad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad using partial update
        Enfermedad partialUpdatedEnfermedad = new Enfermedad();
        partialUpdatedEnfermedad.setId(enfermedad.getId());

        partialUpdatedEnfermedad.descripcion(UPDATED_DESCRIPCION);

        restEnfermedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfermedad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnfermedad))
            )
            .andExpect(status().isOk());

        // Validate the Enfermedad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnfermedadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnfermedad, enfermedad),
            getPersistedEnfermedad(enfermedad)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnfermedadWithPatch() throws Exception {
        // Initialize the database
        enfermedadRepository.saveAndFlush(enfermedad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enfermedad using partial update
        Enfermedad partialUpdatedEnfermedad = new Enfermedad();
        partialUpdatedEnfermedad.setId(enfermedad.getId());

        partialUpdatedEnfermedad.nombre(UPDATED_NOMBRE).descripcion(UPDATED_DESCRIPCION);

        restEnfermedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnfermedad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnfermedad))
            )
            .andExpect(status().isOk());

        // Validate the Enfermedad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnfermedadUpdatableFieldsEquals(partialUpdatedEnfermedad, getPersistedEnfermedad(partialUpdatedEnfermedad));
    }

    @Test
    @Transactional
    void patchNonExistingEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enfermedadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enfermedadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnfermedad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enfermedad.setId(longCount.incrementAndGet());

        // Create the Enfermedad
        EnfermedadDTO enfermedadDTO = enfermedadMapper.toDto(enfermedad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnfermedadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enfermedadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enfermedad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnfermedad() throws Exception {
        // Initialize the database
        enfermedadRepository.saveAndFlush(enfermedad);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enfermedad
        restEnfermedadMockMvc
            .perform(delete(ENTITY_API_URL_ID, enfermedad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enfermedadRepository.count();
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

    protected Enfermedad getPersistedEnfermedad(Enfermedad enfermedad) {
        return enfermedadRepository.findById(enfermedad.getId()).orElseThrow();
    }

    protected void assertPersistedEnfermedadToMatchAllProperties(Enfermedad expectedEnfermedad) {
        assertEnfermedadAllPropertiesEquals(expectedEnfermedad, getPersistedEnfermedad(expectedEnfermedad));
    }

    protected void assertPersistedEnfermedadToMatchUpdatableProperties(Enfermedad expectedEnfermedad) {
        assertEnfermedadAllUpdatablePropertiesEquals(expectedEnfermedad, getPersistedEnfermedad(expectedEnfermedad));
    }
}
