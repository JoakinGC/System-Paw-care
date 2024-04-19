package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.EsteticaDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Estetica}.
 */
public interface EsteticaService {
    /**
     * Save a estetica.
     *
     * @param esteticaDTO the entity to save.
     * @return the persisted entity.
     */
    EsteticaDTO save(EsteticaDTO esteticaDTO);

    /**
     * Updates a estetica.
     *
     * @param esteticaDTO the entity to update.
     * @return the persisted entity.
     */
    EsteticaDTO update(EsteticaDTO esteticaDTO);

    /**
     * Partially updates a estetica.
     *
     * @param esteticaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EsteticaDTO> partialUpdate(EsteticaDTO esteticaDTO);

    /**
     * Get all the esteticas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EsteticaDTO> findAll(Pageable pageable);

    /**
     * Get all the EsteticaDTO where Usuario is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<EsteticaDTO> findAllWhereUsuarioIsNull();

    /**
     * Get the "id" estetica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EsteticaDTO> findOne(Long id);

    /**
     * Delete the "id" estetica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
