package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.EstudiosDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Estudios}.
 */
public interface EstudiosService {
    /**
     * Save a estudios.
     *
     * @param estudiosDTO the entity to save.
     * @return the persisted entity.
     */
    EstudiosDTO save(EstudiosDTO estudiosDTO);

    /**
     * Updates a estudios.
     *
     * @param estudiosDTO the entity to update.
     * @return the persisted entity.
     */
    EstudiosDTO update(EstudiosDTO estudiosDTO);

    /**
     * Partially updates a estudios.
     *
     * @param estudiosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EstudiosDTO> partialUpdate(EstudiosDTO estudiosDTO);

    /**
     * Get all the estudios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EstudiosDTO> findAll(Pageable pageable);

    /**
     * Get all the estudios with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EstudiosDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" estudios.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EstudiosDTO> findOne(Long id);

    /**
     * Delete the "id" estudios.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
