package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.VeterinarioAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Veterinario;
import com.veterinary.sistema.repository.VeterinarioRepository;
import com.veterinary.sistema.service.dto.VeterinarioDTO;
import com.veterinary.sistema.service.mapper.VeterinarioMapper;
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
 * Integration tests for the {@link VeterinarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VeterinarioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBB";

    private static final String DEFAULT_ESPECILIDAD = "AAAAAAAAAA";
    private static final String UPDATED_ESPECILIDAD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/veterinarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Autowired
    private VeterinarioMapper veterinarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVeterinarioMockMvc;

    private Veterinario veterinario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Veterinario createEntity(EntityManager em) {
        Veterinario veterinario = new Veterinario()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .especilidad(DEFAULT_ESPECILIDAD);
        return veterinario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Veterinario createUpdatedEntity(EntityManager em) {
        Veterinario veterinario = new Veterinario()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .especilidad(UPDATED_ESPECILIDAD);
        return veterinario;
    }

    @BeforeEach
    public void initTest() {
        veterinario = createEntity(em);
    }

    @Test
    @Transactional
    void createVeterinario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Veterinario
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(veterinario);
        var returnedVeterinarioDTO = om.readValue(
            restVeterinarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veterinarioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VeterinarioDTO.class
        );

        // Validate the Veterinario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVeterinario = veterinarioMapper.toEntity(returnedVeterinarioDTO);
        assertVeterinarioUpdatableFieldsEquals(returnedVeterinario, getPersistedVeterinario(returnedVeterinario));
    }

    @Test
    @Transactional
    void createVeterinarioWithExistingId() throws Exception {
        // Create the Veterinario with an existing ID
        veterinario.setId(1L);
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(veterinario);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVeterinarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veterinarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Veterinario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVeterinarios() throws Exception {
        // Initialize the database
        veterinarioRepository.saveAndFlush(veterinario);

        // Get all the veterinarioList
        restVeterinarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veterinario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].especilidad").value(hasItem(DEFAULT_ESPECILIDAD)));
    }

    @Test
    @Transactional
    void getVeterinario() throws Exception {
        // Initialize the database
        veterinarioRepository.saveAndFlush(veterinario);

        // Get the veterinario
        restVeterinarioMockMvc
            .perform(get(ENTITY_API_URL_ID, veterinario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(veterinario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.especilidad").value(DEFAULT_ESPECILIDAD));
    }

    @Test
    @Transactional
    void getNonExistingVeterinario() throws Exception {
        // Get the veterinario
        restVeterinarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVeterinario() throws Exception {
        // Initialize the database
        veterinarioRepository.saveAndFlush(veterinario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the veterinario
        Veterinario updatedVeterinario = veterinarioRepository.findById(veterinario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVeterinario are not directly saved in db
        em.detach(updatedVeterinario);
        updatedVeterinario
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .especilidad(UPDATED_ESPECILIDAD);
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(updatedVeterinario);

        restVeterinarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, veterinarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(veterinarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Veterinario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVeterinarioToMatchAllProperties(updatedVeterinario);
    }

    @Test
    @Transactional
    void putNonExistingVeterinario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veterinario.setId(longCount.incrementAndGet());

        // Create the Veterinario
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(veterinario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeterinarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, veterinarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(veterinarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veterinario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVeterinario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veterinario.setId(longCount.incrementAndGet());

        // Create the Veterinario
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(veterinario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeterinarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(veterinarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veterinario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVeterinario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veterinario.setId(longCount.incrementAndGet());

        // Create the Veterinario
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(veterinario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeterinarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(veterinarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Veterinario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVeterinarioWithPatch() throws Exception {
        // Initialize the database
        veterinarioRepository.saveAndFlush(veterinario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the veterinario using partial update
        Veterinario partialUpdatedVeterinario = new Veterinario();
        partialUpdatedVeterinario.setId(veterinario.getId());

        partialUpdatedVeterinario.nombre(UPDATED_NOMBRE).apellido(UPDATED_APELLIDO).direccion(UPDATED_DIRECCION).telefono(UPDATED_TELEFONO);

        restVeterinarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVeterinario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVeterinario))
            )
            .andExpect(status().isOk());

        // Validate the Veterinario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVeterinarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVeterinario, veterinario),
            getPersistedVeterinario(veterinario)
        );
    }

    @Test
    @Transactional
    void fullUpdateVeterinarioWithPatch() throws Exception {
        // Initialize the database
        veterinarioRepository.saveAndFlush(veterinario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the veterinario using partial update
        Veterinario partialUpdatedVeterinario = new Veterinario();
        partialUpdatedVeterinario.setId(veterinario.getId());

        partialUpdatedVeterinario
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .especilidad(UPDATED_ESPECILIDAD);

        restVeterinarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVeterinario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVeterinario))
            )
            .andExpect(status().isOk());

        // Validate the Veterinario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVeterinarioUpdatableFieldsEquals(partialUpdatedVeterinario, getPersistedVeterinario(partialUpdatedVeterinario));
    }

    @Test
    @Transactional
    void patchNonExistingVeterinario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veterinario.setId(longCount.incrementAndGet());

        // Create the Veterinario
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(veterinario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeterinarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, veterinarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(veterinarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veterinario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVeterinario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veterinario.setId(longCount.incrementAndGet());

        // Create the Veterinario
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(veterinario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeterinarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(veterinarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Veterinario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVeterinario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        veterinario.setId(longCount.incrementAndGet());

        // Create the Veterinario
        VeterinarioDTO veterinarioDTO = veterinarioMapper.toDto(veterinario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeterinarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(veterinarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Veterinario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVeterinario() throws Exception {
        // Initialize the database
        veterinarioRepository.saveAndFlush(veterinario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the veterinario
        restVeterinarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, veterinario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return veterinarioRepository.count();
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

    protected Veterinario getPersistedVeterinario(Veterinario veterinario) {
        return veterinarioRepository.findById(veterinario.getId()).orElseThrow();
    }

    protected void assertPersistedVeterinarioToMatchAllProperties(Veterinario expectedVeterinario) {
        assertVeterinarioAllPropertiesEquals(expectedVeterinario, getPersistedVeterinario(expectedVeterinario));
    }

    protected void assertPersistedVeterinarioToMatchUpdatableProperties(Veterinario expectedVeterinario) {
        assertVeterinarioAllUpdatablePropertiesEquals(expectedVeterinario, getPersistedVeterinario(expectedVeterinario));
    }
}
