package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.RazaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Raza}.
 */
public interface RazaService {
    /**
     * Save a raza.
     *
     * @param razaDTO the entity to save.
     * @return the persisted entity.
     */
    RazaDTO save(RazaDTO razaDTO);

    /**
     * Updates a raza.
     *
     * @param razaDTO the entity to update.
     * @return the persisted entity.
     */
    RazaDTO update(RazaDTO razaDTO);

    /**
     * Partially updates a raza.
     *
     * @param razaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RazaDTO> partialUpdate(RazaDTO razaDTO);

    /**
     * Get all the razas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RazaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" raza.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RazaDTO> findOne(Long id);

    /**
     * Delete the "id" raza.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
