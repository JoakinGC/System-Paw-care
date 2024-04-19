package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.TratamientoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Tratamiento}.
 */
public interface TratamientoService {
    /**
     * Save a tratamiento.
     *
     * @param tratamientoDTO the entity to save.
     * @return the persisted entity.
     */
    TratamientoDTO save(TratamientoDTO tratamientoDTO);

    /**
     * Updates a tratamiento.
     *
     * @param tratamientoDTO the entity to update.
     * @return the persisted entity.
     */
    TratamientoDTO update(TratamientoDTO tratamientoDTO);

    /**
     * Partially updates a tratamiento.
     *
     * @param tratamientoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TratamientoDTO> partialUpdate(TratamientoDTO tratamientoDTO);

    /**
     * Get all the tratamientos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TratamientoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tratamiento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TratamientoDTO> findOne(Long id);

    /**
     * Delete the "id" tratamiento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
