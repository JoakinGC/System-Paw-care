package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.EnfermedadDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Enfermedad}.
 */
public interface EnfermedadService {
    /**
     * Save a enfermedad.
     *
     * @param enfermedadDTO the entity to save.
     * @return the persisted entity.
     */
    EnfermedadDTO save(EnfermedadDTO enfermedadDTO);

    /**
     * Updates a enfermedad.
     *
     * @param enfermedadDTO the entity to update.
     * @return the persisted entity.
     */
    EnfermedadDTO update(EnfermedadDTO enfermedadDTO);

    /**
     * Partially updates a enfermedad.
     *
     * @param enfermedadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnfermedadDTO> partialUpdate(EnfermedadDTO enfermedadDTO);

    /**
     * Get all the enfermedads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnfermedadDTO> findAll(Pageable pageable);

    /**
     * Get all the enfermedads with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EnfermedadDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" enfermedad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnfermedadDTO> findOne(Long id);

    /**
     * Delete the "id" enfermedad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
