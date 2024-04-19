package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.HistorialDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Historial}.
 */
public interface HistorialService {
    /**
     * Save a historial.
     *
     * @param historialDTO the entity to save.
     * @return the persisted entity.
     */
    HistorialDTO save(HistorialDTO historialDTO);

    /**
     * Updates a historial.
     *
     * @param historialDTO the entity to update.
     * @return the persisted entity.
     */
    HistorialDTO update(HistorialDTO historialDTO);

    /**
     * Partially updates a historial.
     *
     * @param historialDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistorialDTO> partialUpdate(HistorialDTO historialDTO);

    /**
     * Get all the historials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistorialDTO> findAll(Pageable pageable);

    /**
     * Get all the historials with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HistorialDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" historial.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistorialDTO> findOne(Long id);

    /**
     * Delete the "id" historial.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
