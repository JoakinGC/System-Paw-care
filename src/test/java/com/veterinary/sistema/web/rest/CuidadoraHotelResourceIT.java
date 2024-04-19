package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.CuidadoraHotelAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.CuidadoraHotel;
import com.veterinary.sistema.repository.CuidadoraHotelRepository;
import com.veterinary.sistema.service.dto.CuidadoraHotelDTO;
import com.veterinary.sistema.service.mapper.CuidadoraHotelMapper;
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
 * Integration tests for the {@link CuidadoraHotelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CuidadoraHotelResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBB";

    private static final String DEFAULT_SERVICIO_OFRECIDO = "AAAAAAAAAA";
    private static final String UPDATED_SERVICIO_OFRECIDO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cuidadora-hotels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CuidadoraHotelRepository cuidadoraHotelRepository;

    @Autowired
    private CuidadoraHotelMapper cuidadoraHotelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCuidadoraHotelMockMvc;

    private CuidadoraHotel cuidadoraHotel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuidadoraHotel createEntity(EntityManager em) {
        CuidadoraHotel cuidadoraHotel = new CuidadoraHotel()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .servicioOfrecido(DEFAULT_SERVICIO_OFRECIDO);
        return cuidadoraHotel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuidadoraHotel createUpdatedEntity(EntityManager em) {
        CuidadoraHotel cuidadoraHotel = new CuidadoraHotel()
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .servicioOfrecido(UPDATED_SERVICIO_OFRECIDO);
        return cuidadoraHotel;
    }

    @BeforeEach
    public void initTest() {
        cuidadoraHotel = createEntity(em);
    }

    @Test
    @Transactional
    void createCuidadoraHotel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CuidadoraHotel
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(cuidadoraHotel);
        var returnedCuidadoraHotelDTO = om.readValue(
            restCuidadoraHotelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuidadoraHotelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CuidadoraHotelDTO.class
        );

        // Validate the CuidadoraHotel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCuidadoraHotel = cuidadoraHotelMapper.toEntity(returnedCuidadoraHotelDTO);
        assertCuidadoraHotelUpdatableFieldsEquals(returnedCuidadoraHotel, getPersistedCuidadoraHotel(returnedCuidadoraHotel));
    }

    @Test
    @Transactional
    void createCuidadoraHotelWithExistingId() throws Exception {
        // Create the CuidadoraHotel with an existing ID
        cuidadoraHotel.setId(1L);
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(cuidadoraHotel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuidadoraHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuidadoraHotelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CuidadoraHotel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCuidadoraHotels() throws Exception {
        // Initialize the database
        cuidadoraHotelRepository.saveAndFlush(cuidadoraHotel);

        // Get all the cuidadoraHotelList
        restCuidadoraHotelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuidadoraHotel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].servicioOfrecido").value(hasItem(DEFAULT_SERVICIO_OFRECIDO)));
    }

    @Test
    @Transactional
    void getCuidadoraHotel() throws Exception {
        // Initialize the database
        cuidadoraHotelRepository.saveAndFlush(cuidadoraHotel);

        // Get the cuidadoraHotel
        restCuidadoraHotelMockMvc
            .perform(get(ENTITY_API_URL_ID, cuidadoraHotel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cuidadoraHotel.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.servicioOfrecido").value(DEFAULT_SERVICIO_OFRECIDO));
    }

    @Test
    @Transactional
    void getNonExistingCuidadoraHotel() throws Exception {
        // Get the cuidadoraHotel
        restCuidadoraHotelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCuidadoraHotel() throws Exception {
        // Initialize the database
        cuidadoraHotelRepository.saveAndFlush(cuidadoraHotel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuidadoraHotel
        CuidadoraHotel updatedCuidadoraHotel = cuidadoraHotelRepository.findById(cuidadoraHotel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCuidadoraHotel are not directly saved in db
        em.detach(updatedCuidadoraHotel);
        updatedCuidadoraHotel
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .servicioOfrecido(UPDATED_SERVICIO_OFRECIDO);
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(updatedCuidadoraHotel);

        restCuidadoraHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuidadoraHotelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cuidadoraHotelDTO))
            )
            .andExpect(status().isOk());

        // Validate the CuidadoraHotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCuidadoraHotelToMatchAllProperties(updatedCuidadoraHotel);
    }

    @Test
    @Transactional
    void putNonExistingCuidadoraHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuidadoraHotel.setId(longCount.incrementAndGet());

        // Create the CuidadoraHotel
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(cuidadoraHotel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuidadoraHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuidadoraHotelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cuidadoraHotelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuidadoraHotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCuidadoraHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuidadoraHotel.setId(longCount.incrementAndGet());

        // Create the CuidadoraHotel
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(cuidadoraHotel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuidadoraHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cuidadoraHotelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuidadoraHotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCuidadoraHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuidadoraHotel.setId(longCount.incrementAndGet());

        // Create the CuidadoraHotel
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(cuidadoraHotel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuidadoraHotelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuidadoraHotelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CuidadoraHotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCuidadoraHotelWithPatch() throws Exception {
        // Initialize the database
        cuidadoraHotelRepository.saveAndFlush(cuidadoraHotel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuidadoraHotel using partial update
        CuidadoraHotel partialUpdatedCuidadoraHotel = new CuidadoraHotel();
        partialUpdatedCuidadoraHotel.setId(cuidadoraHotel.getId());

        partialUpdatedCuidadoraHotel
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .servicioOfrecido(UPDATED_SERVICIO_OFRECIDO);

        restCuidadoraHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuidadoraHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCuidadoraHotel))
            )
            .andExpect(status().isOk());

        // Validate the CuidadoraHotel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCuidadoraHotelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCuidadoraHotel, cuidadoraHotel),
            getPersistedCuidadoraHotel(cuidadoraHotel)
        );
    }

    @Test
    @Transactional
    void fullUpdateCuidadoraHotelWithPatch() throws Exception {
        // Initialize the database
        cuidadoraHotelRepository.saveAndFlush(cuidadoraHotel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cuidadoraHotel using partial update
        CuidadoraHotel partialUpdatedCuidadoraHotel = new CuidadoraHotel();
        partialUpdatedCuidadoraHotel.setId(cuidadoraHotel.getId());

        partialUpdatedCuidadoraHotel
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .servicioOfrecido(UPDATED_SERVICIO_OFRECIDO);

        restCuidadoraHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuidadoraHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCuidadoraHotel))
            )
            .andExpect(status().isOk());

        // Validate the CuidadoraHotel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCuidadoraHotelUpdatableFieldsEquals(partialUpdatedCuidadoraHotel, getPersistedCuidadoraHotel(partialUpdatedCuidadoraHotel));
    }

    @Test
    @Transactional
    void patchNonExistingCuidadoraHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuidadoraHotel.setId(longCount.incrementAndGet());

        // Create the CuidadoraHotel
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(cuidadoraHotel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuidadoraHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cuidadoraHotelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cuidadoraHotelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuidadoraHotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCuidadoraHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuidadoraHotel.setId(longCount.incrementAndGet());

        // Create the CuidadoraHotel
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(cuidadoraHotel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuidadoraHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cuidadoraHotelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuidadoraHotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCuidadoraHotel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cuidadoraHotel.setId(longCount.incrementAndGet());

        // Create the CuidadoraHotel
        CuidadoraHotelDTO cuidadoraHotelDTO = cuidadoraHotelMapper.toDto(cuidadoraHotel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuidadoraHotelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cuidadoraHotelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CuidadoraHotel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCuidadoraHotel() throws Exception {
        // Initialize the database
        cuidadoraHotelRepository.saveAndFlush(cuidadoraHotel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cuidadoraHotel
        restCuidadoraHotelMockMvc
            .perform(delete(ENTITY_API_URL_ID, cuidadoraHotel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cuidadoraHotelRepository.count();
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

    protected CuidadoraHotel getPersistedCuidadoraHotel(CuidadoraHotel cuidadoraHotel) {
        return cuidadoraHotelRepository.findById(cuidadoraHotel.getId()).orElseThrow();
    }

    protected void assertPersistedCuidadoraHotelToMatchAllProperties(CuidadoraHotel expectedCuidadoraHotel) {
        assertCuidadoraHotelAllPropertiesEquals(expectedCuidadoraHotel, getPersistedCuidadoraHotel(expectedCuidadoraHotel));
    }

    protected void assertPersistedCuidadoraHotelToMatchUpdatableProperties(CuidadoraHotel expectedCuidadoraHotel) {
        assertCuidadoraHotelAllUpdatablePropertiesEquals(expectedCuidadoraHotel, getPersistedCuidadoraHotel(expectedCuidadoraHotel));
    }
}
