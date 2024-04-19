package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.DuenoDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Dueno}.
 */
public interface DuenoService {
    /**
     * Save a dueno.
     *
     * @param duenoDTO the entity to save.
     * @return the persisted entity.
     */
    DuenoDTO save(DuenoDTO duenoDTO);

    /**
     * Updates a dueno.
     *
     * @param duenoDTO the entity to update.
     * @return the persisted entity.
     */
    DuenoDTO update(DuenoDTO duenoDTO);

    /**
     * Partially updates a dueno.
     *
     * @param duenoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DuenoDTO> partialUpdate(DuenoDTO duenoDTO);

    /**
     * Get all the duenos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DuenoDTO> findAll(Pageable pageable);

    /**
     * Get all the DuenoDTO where Usuario is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<DuenoDTO> findAllWhereUsuarioIsNull();

    /**
     * Get the "id" dueno.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DuenoDTO> findOne(Long id);

    /**
     * Delete the "id" dueno.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
