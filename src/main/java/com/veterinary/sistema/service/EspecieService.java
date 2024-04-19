package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.EspecieDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Especie}.
 */
public interface EspecieService {
    /**
     * Save a especie.
     *
     * @param especieDTO the entity to save.
     * @return the persisted entity.
     */
    EspecieDTO save(EspecieDTO especieDTO);

    /**
     * Updates a especie.
     *
     * @param especieDTO the entity to update.
     * @return the persisted entity.
     */
    EspecieDTO update(EspecieDTO especieDTO);

    /**
     * Partially updates a especie.
     *
     * @param especieDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EspecieDTO> partialUpdate(EspecieDTO especieDTO);

    /**
     * Get all the especies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EspecieDTO> findAll(Pageable pageable);

    /**
     * Get the "id" especie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EspecieDTO> findOne(Long id);

    /**
     * Delete the "id" especie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
