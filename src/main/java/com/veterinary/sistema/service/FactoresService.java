package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.FactoresDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Factores}.
 */
public interface FactoresService {
    /**
     * Save a factores.
     *
     * @param factoresDTO the entity to save.
     * @return the persisted entity.
     */
    FactoresDTO save(FactoresDTO factoresDTO);

    /**
     * Updates a factores.
     *
     * @param factoresDTO the entity to update.
     * @return the persisted entity.
     */
    FactoresDTO update(FactoresDTO factoresDTO);

    /**
     * Partially updates a factores.
     *
     * @param factoresDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FactoresDTO> partialUpdate(FactoresDTO factoresDTO);

    /**
     * Get all the factores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FactoresDTO> findAll(Pageable pageable);

    /**
     * Get the "id" factores.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FactoresDTO> findOne(Long id);

    /**
     * Delete the "id" factores.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
