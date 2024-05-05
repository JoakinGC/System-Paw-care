package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.MascotaAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.Mascota;
import com.veterinary.sistema.repository.MascotaRepository;
import com.veterinary.sistema.service.MascotaService;
import com.veterinary.sistema.service.dto.MascotaDTO;
import com.veterinary.sistema.service.mapper.MascotaMapper;
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
 * Integration tests for the {@link MascotaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class  MascotaResourceIT {

    private static final Integer DEFAULT_N_IDENTIFICACION_CARNET = 1;
    private static final Integer UPDATED_N_IDENTIFICACION_CARNET = 2;

    private static final String DEFAULT_FOTO = "AAAAAAAAAA";
    private static final String UPDATED_FOTO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/mascotas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Mock
    private MascotaRepository mascotaRepositoryMock;

    @Autowired
    private MascotaMapper mascotaMapper;

    @Mock
    private MascotaService mascotaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMascotaMockMvc;

    private Mascota mascota;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mascota createEntity(EntityManager em) {
        Mascota mascota = new Mascota()
            .nIdentificacionCarnet(DEFAULT_N_IDENTIFICACION_CARNET)
            .foto(DEFAULT_FOTO)
            .fechaNacimiento(DEFAULT_FECHA_NACIMIENTO);
        return mascota;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mascota createUpdatedEntity(EntityManager em) {
        Mascota mascota = new Mascota()
            .nIdentificacionCarnet(UPDATED_N_IDENTIFICACION_CARNET)
            .foto(UPDATED_FOTO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        return mascota;
    }

    @BeforeEach
    public void initTest() {
        mascota = createEntity(em);
    }

    @Test
    @Transactional
    void createMascota() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);
        var returnedMascotaDTO = om.readValue(
            restMascotaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mascotaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MascotaDTO.class
        );

        // Validate the Mascota in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMascota = mascotaMapper.toEntity(returnedMascotaDTO);
        assertMascotaUpdatableFieldsEquals(returnedMascota, getPersistedMascota(returnedMascota));
    }

    @Test
    @Transactional
    void createMascotaWithExistingId() throws Exception {
        // Create the Mascota with an existing ID
        mascota.setId(1L);
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMascotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mascotaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checknIdentificacionCarnetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mascota.setnIdentificacionCarnet(null);

        // Create the Mascota, which fails.
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        restMascotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mascotaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFotoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mascota.setFoto(null);

        // Create the Mascota, which fails.
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        restMascotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mascotaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaNacimientoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mascota.setFechaNacimiento(null);

        // Create the Mascota, which fails.
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        restMascotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mascotaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMascotas() throws Exception {
        // Initialize the database
        mascotaRepository.saveAndFlush(mascota);

        // Get all the mascotaList
        restMascotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mascota.getId().intValue())))
            .andExpect(jsonPath("$.[*].nIdentificacionCarnet").value(hasItem(DEFAULT_N_IDENTIFICACION_CARNET)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.[*].fechaNacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMascotasWithEagerRelationshipsIsEnabled() throws Exception {
        when(mascotaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMascotaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mascotaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMascotasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mascotaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMascotaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(mascotaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMascota() throws Exception {
        // Initialize the database
        mascotaRepository.saveAndFlush(mascota);

        // Get the mascota
        restMascotaMockMvc
            .perform(get(ENTITY_API_URL_ID, mascota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mascota.getId().intValue()))
            .andExpect(jsonPath("$.nIdentificacionCarnet").value(DEFAULT_N_IDENTIFICACION_CARNET))
            .andExpect(jsonPath("$.foto").value(DEFAULT_FOTO))
            .andExpect(jsonPath("$.fechaNacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMascota() throws Exception {
        // Get the mascota
        restMascotaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMascota() throws Exception {
        // Initialize the database
        mascotaRepository.saveAndFlush(mascota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mascota
        Mascota updatedMascota = mascotaRepository.findById(mascota.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMascota are not directly saved in db
        em.detach(updatedMascota);
        updatedMascota.nIdentificacionCarnet(UPDATED_N_IDENTIFICACION_CARNET).foto(UPDATED_FOTO).fechaNacimiento(UPDATED_FECHA_NACIMIENTO);
        MascotaDTO mascotaDTO = mascotaMapper.toDto(updatedMascota);

        restMascotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mascotaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mascotaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMascotaToMatchAllProperties(updatedMascota);
    }

    @Test
    @Transactional
    void putNonExistingMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(longCount.incrementAndGet());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMascotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mascotaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mascotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(longCount.incrementAndGet());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMascotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mascotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(longCount.incrementAndGet());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMascotaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mascotaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMascotaWithPatch() throws Exception {
        // Initialize the database
        mascotaRepository.saveAndFlush(mascota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mascota using partial update
        Mascota partialUpdatedMascota = new Mascota();
        partialUpdatedMascota.setId(mascota.getId());

        partialUpdatedMascota.nIdentificacionCarnet(UPDATED_N_IDENTIFICACION_CARNET).foto(UPDATED_FOTO);

        restMascotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMascota.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMascota))
            )
            .andExpect(status().isOk());

        // Validate the Mascota in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMascotaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMascota, mascota), getPersistedMascota(mascota));
    }

    @Test
    @Transactional
    void fullUpdateMascotaWithPatch() throws Exception {
        // Initialize the database
        mascotaRepository.saveAndFlush(mascota);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mascota using partial update
        Mascota partialUpdatedMascota = new Mascota();
        partialUpdatedMascota.setId(mascota.getId());

        partialUpdatedMascota
            .nIdentificacionCarnet(UPDATED_N_IDENTIFICACION_CARNET)
            .foto(UPDATED_FOTO)
            .fechaNacimiento(UPDATED_FECHA_NACIMIENTO);

        restMascotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMascota.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMascota))
            )
            .andExpect(status().isOk());

        // Validate the Mascota in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMascotaUpdatableFieldsEquals(partialUpdatedMascota, getPersistedMascota(partialUpdatedMascota));
    }

    @Test
    @Transactional
    void patchNonExistingMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(longCount.incrementAndGet());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMascotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mascotaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mascotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(longCount.incrementAndGet());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMascotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mascotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMascota() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mascota.setId(longCount.incrementAndGet());

        // Create the Mascota
        MascotaDTO mascotaDTO = mascotaMapper.toDto(mascota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMascotaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mascotaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mascota in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMascota() throws Exception {
        // Initialize the database
        mascotaRepository.saveAndFlush(mascota);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mascota
        restMascotaMockMvc
            .perform(delete(ENTITY_API_URL_ID, mascota.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mascotaRepository.count();
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

    protected Mascota getPersistedMascota(Mascota mascota) {
        return mascotaRepository.findById(mascota.getId()).orElseThrow();
    }

    protected void assertPersistedMascotaToMatchAllProperties(Mascota expectedMascota) {
        assertMascotaAllPropertiesEquals(expectedMascota, getPersistedMascota(expectedMascota));
    }

    protected void assertPersistedMascotaToMatchUpdatableProperties(Mascota expectedMascota) {
        assertMascotaAllUpdatablePropertiesEquals(expectedMascota, getPersistedMascota(expectedMascota));
    }
}
