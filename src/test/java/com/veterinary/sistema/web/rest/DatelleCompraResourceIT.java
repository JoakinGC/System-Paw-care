package com.veterinary.sistema.web.rest;

import static com.veterinary.sistema.domain.DatelleCompraAsserts.*;
import static com.veterinary.sistema.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.veterinary.sistema.IntegrationTest;
import com.veterinary.sistema.domain.DatelleCompra;
import com.veterinary.sistema.repository.DatelleCompraRepository;
import com.veterinary.sistema.service.dto.DatelleCompraDTO;
import com.veterinary.sistema.service.mapper.DatelleCompraMapper;
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
 * Integration tests for the {@link DatelleCompraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DatelleCompraResourceIT {

    private static final Float DEFAULT_CANTIDAD = 1F;
    private static final Float UPDATED_CANTIDAD = 2F;

    private static final Float DEFAULT_PRECIO_UNITARIO = 1F;
    private static final Float UPDATED_PRECIO_UNITARIO = 2F;

    private static final Float DEFAULT_TOTAL_PRODUCTO = 1F;
    private static final Float UPDATED_TOTAL_PRODUCTO = 2F;

    private static final String ENTITY_API_URL = "/api/datelle-compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DatelleCompraRepository datelleCompraRepository;

    @Autowired
    private DatelleCompraMapper datelleCompraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDatelleCompraMockMvc;

    private DatelleCompra datelleCompra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatelleCompra createEntity(EntityManager em) {
        DatelleCompra datelleCompra = new DatelleCompra()
            .cantidad(DEFAULT_CANTIDAD)
            .precioUnitario(DEFAULT_PRECIO_UNITARIO)
            .totalProducto(DEFAULT_TOTAL_PRODUCTO);
        return datelleCompra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatelleCompra createUpdatedEntity(EntityManager em) {
        DatelleCompra datelleCompra = new DatelleCompra()
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .totalProducto(UPDATED_TOTAL_PRODUCTO);
        return datelleCompra;
    }

    @BeforeEach
    public void initTest() {
        datelleCompra = createEntity(em);
    }

    @Test
    @Transactional
    void createDatelleCompra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DatelleCompra
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);
        var returnedDatelleCompraDTO = om.readValue(
            restDatelleCompraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datelleCompraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DatelleCompraDTO.class
        );

        // Validate the DatelleCompra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDatelleCompra = datelleCompraMapper.toEntity(returnedDatelleCompraDTO);
        assertDatelleCompraUpdatableFieldsEquals(returnedDatelleCompra, getPersistedDatelleCompra(returnedDatelleCompra));
    }

    @Test
    @Transactional
    void createDatelleCompraWithExistingId() throws Exception {
        // Create the DatelleCompra with an existing ID
        datelleCompra.setId(1L);
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatelleCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datelleCompraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DatelleCompra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datelleCompra.setCantidad(null);

        // Create the DatelleCompra, which fails.
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        restDatelleCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datelleCompraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioUnitarioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datelleCompra.setPrecioUnitario(null);

        // Create the DatelleCompra, which fails.
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        restDatelleCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datelleCompraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalProductoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datelleCompra.setTotalProducto(null);

        // Create the DatelleCompra, which fails.
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        restDatelleCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datelleCompraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDatelleCompras() throws Exception {
        // Initialize the database
        datelleCompraRepository.saveAndFlush(datelleCompra);

        // Get all the datelleCompraList
        restDatelleCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datelleCompra.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.doubleValue())))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(DEFAULT_PRECIO_UNITARIO.doubleValue())))
            .andExpect(jsonPath("$.[*].totalProducto").value(hasItem(DEFAULT_TOTAL_PRODUCTO.doubleValue())));
    }

    @Test
    @Transactional
    void getDatelleCompra() throws Exception {
        // Initialize the database
        datelleCompraRepository.saveAndFlush(datelleCompra);

        // Get the datelleCompra
        restDatelleCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, datelleCompra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(datelleCompra.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.doubleValue()))
            .andExpect(jsonPath("$.precioUnitario").value(DEFAULT_PRECIO_UNITARIO.doubleValue()))
            .andExpect(jsonPath("$.totalProducto").value(DEFAULT_TOTAL_PRODUCTO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDatelleCompra() throws Exception {
        // Get the datelleCompra
        restDatelleCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDatelleCompra() throws Exception {
        // Initialize the database
        datelleCompraRepository.saveAndFlush(datelleCompra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datelleCompra
        DatelleCompra updatedDatelleCompra = datelleCompraRepository.findById(datelleCompra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDatelleCompra are not directly saved in db
        em.detach(updatedDatelleCompra);
        updatedDatelleCompra.cantidad(UPDATED_CANTIDAD).precioUnitario(UPDATED_PRECIO_UNITARIO).totalProducto(UPDATED_TOTAL_PRODUCTO);
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(updatedDatelleCompra);

        restDatelleCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, datelleCompraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datelleCompraDTO))
            )
            .andExpect(status().isOk());

        // Validate the DatelleCompra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDatelleCompraToMatchAllProperties(updatedDatelleCompra);
    }

    @Test
    @Transactional
    void putNonExistingDatelleCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datelleCompra.setId(longCount.incrementAndGet());

        // Create the DatelleCompra
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatelleCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, datelleCompraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datelleCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatelleCompra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDatelleCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datelleCompra.setId(longCount.incrementAndGet());

        // Create the DatelleCompra
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatelleCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datelleCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatelleCompra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDatelleCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datelleCompra.setId(longCount.incrementAndGet());

        // Create the DatelleCompra
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatelleCompraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datelleCompraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DatelleCompra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDatelleCompraWithPatch() throws Exception {
        // Initialize the database
        datelleCompraRepository.saveAndFlush(datelleCompra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datelleCompra using partial update
        DatelleCompra partialUpdatedDatelleCompra = new DatelleCompra();
        partialUpdatedDatelleCompra.setId(datelleCompra.getId());

        partialUpdatedDatelleCompra.precioUnitario(UPDATED_PRECIO_UNITARIO);

        restDatelleCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDatelleCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDatelleCompra))
            )
            .andExpect(status().isOk());

        // Validate the DatelleCompra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDatelleCompraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDatelleCompra, datelleCompra),
            getPersistedDatelleCompra(datelleCompra)
        );
    }

    @Test
    @Transactional
    void fullUpdateDatelleCompraWithPatch() throws Exception {
        // Initialize the database
        datelleCompraRepository.saveAndFlush(datelleCompra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datelleCompra using partial update
        DatelleCompra partialUpdatedDatelleCompra = new DatelleCompra();
        partialUpdatedDatelleCompra.setId(datelleCompra.getId());

        partialUpdatedDatelleCompra
            .cantidad(UPDATED_CANTIDAD)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .totalProducto(UPDATED_TOTAL_PRODUCTO);

        restDatelleCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDatelleCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDatelleCompra))
            )
            .andExpect(status().isOk());

        // Validate the DatelleCompra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDatelleCompraUpdatableFieldsEquals(partialUpdatedDatelleCompra, getPersistedDatelleCompra(partialUpdatedDatelleCompra));
    }

    @Test
    @Transactional
    void patchNonExistingDatelleCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datelleCompra.setId(longCount.incrementAndGet());

        // Create the DatelleCompra
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatelleCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, datelleCompraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(datelleCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatelleCompra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDatelleCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datelleCompra.setId(longCount.incrementAndGet());

        // Create the DatelleCompra
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatelleCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(datelleCompraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatelleCompra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDatelleCompra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datelleCompra.setId(longCount.incrementAndGet());

        // Create the DatelleCompra
        DatelleCompraDTO datelleCompraDTO = datelleCompraMapper.toDto(datelleCompra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatelleCompraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(datelleCompraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DatelleCompra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDatelleCompra() throws Exception {
        // Initialize the database
        datelleCompraRepository.saveAndFlush(datelleCompra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the datelleCompra
        restDatelleCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, datelleCompra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return datelleCompraRepository.count();
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

    protected DatelleCompra getPersistedDatelleCompra(DatelleCompra datelleCompra) {
        return datelleCompraRepository.findById(datelleCompra.getId()).orElseThrow();
    }

    protected void assertPersistedDatelleCompraToMatchAllProperties(DatelleCompra expectedDatelleCompra) {
        assertDatelleCompraAllPropertiesEquals(expectedDatelleCompra, getPersistedDatelleCompra(expectedDatelleCompra));
    }

    protected void assertPersistedDatelleCompraToMatchUpdatableProperties(DatelleCompra expectedDatelleCompra) {
        assertDatelleCompraAllUpdatablePropertiesEquals(expectedDatelleCompra, getPersistedDatelleCompra(expectedDatelleCompra));
    }
}
