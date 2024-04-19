package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.VeterinarioDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Veterinario}.
 */
public interface VeterinarioService {
    /**
     * Save a veterinario.
     *
     * @param veterinarioDTO the entity to save.
     * @return the persisted entity.
     */
    VeterinarioDTO save(VeterinarioDTO veterinarioDTO);

    /**
     * Updates a veterinario.
     *
     * @param veterinarioDTO the entity to update.
     * @return the persisted entity.
     */
    VeterinarioDTO update(VeterinarioDTO veterinarioDTO);

    /**
     * Partially updates a veterinario.
     *
     * @param veterinarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VeterinarioDTO> partialUpdate(VeterinarioDTO veterinarioDTO);

    /**
     * Get all the veterinarios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VeterinarioDTO> findAll(Pageable pageable);

    /**
     * Get all the VeterinarioDTO where Usuario is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<VeterinarioDTO> findAllWhereUsuarioIsNull();

    /**
     * Get the "id" veterinario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VeterinarioDTO> findOne(Long id);

    /**
     * Delete the "id" veterinario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
