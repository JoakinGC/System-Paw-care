package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.MascotaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Mascota}.
 */
public interface MascotaService {
    /**
     * Save a mascota.
     *
     * @param mascotaDTO the entity to save.
     * @return the persisted entity.
     */
    MascotaDTO save(MascotaDTO mascotaDTO);

    /**
     * Updates a mascota.
     *
     * @param mascotaDTO the entity to update.
     * @return the persisted entity.
     */
    MascotaDTO update(MascotaDTO mascotaDTO);

    /**
     * Partially updates a mascota.
     *
     * @param mascotaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MascotaDTO> partialUpdate(MascotaDTO mascotaDTO);

    /**
     * Get all the mascotas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MascotaDTO> findAll(Pageable pageable);

    /**
     * Get all the mascotas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MascotaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" mascota.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MascotaDTO> findOne(Long id);

    /**
     * Delete the "id" mascota.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
