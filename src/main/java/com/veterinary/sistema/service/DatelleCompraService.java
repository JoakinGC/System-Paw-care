package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.DatelleCompraDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.DatelleCompra}.
 */
public interface DatelleCompraService {
    /**
     * Save a datelleCompra.
     *
     * @param datelleCompraDTO the entity to save.
     * @return the persisted entity.
     */
    DatelleCompraDTO save(DatelleCompraDTO datelleCompraDTO);

    /**
     * Updates a datelleCompra.
     *
     * @param datelleCompraDTO the entity to update.
     * @return the persisted entity.
     */
    DatelleCompraDTO update(DatelleCompraDTO datelleCompraDTO);

    /**
     * Partially updates a datelleCompra.
     *
     * @param datelleCompraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DatelleCompraDTO> partialUpdate(DatelleCompraDTO datelleCompraDTO);

    /**
     * Get all the datelleCompras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DatelleCompraDTO> findAll(Pageable pageable);

    /**
     * Get the "id" datelleCompra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DatelleCompraDTO> findOne(Long id);

    /**
     * Delete the "id" datelleCompra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
