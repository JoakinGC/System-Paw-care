package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.TerapiaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Terapia}.
 */
public interface TerapiaService {
    /**
     * Save a terapia.
     *
     * @param terapiaDTO the entity to save.
     * @return the persisted entity.
     */
    TerapiaDTO save(TerapiaDTO terapiaDTO);

    /**
     * Updates a terapia.
     *
     * @param terapiaDTO the entity to update.
     * @return the persisted entity.
     */
    TerapiaDTO update(TerapiaDTO terapiaDTO);

    /**
     * Partially updates a terapia.
     *
     * @param terapiaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TerapiaDTO> partialUpdate(TerapiaDTO terapiaDTO);

    /**
     * Get all the terapias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TerapiaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" terapia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TerapiaDTO> findOne(Long id);

    /**
     * Delete the "id" terapia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
