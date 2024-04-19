package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.MedicamentoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.Medicamento}.
 */
public interface MedicamentoService {
    /**
     * Save a medicamento.
     *
     * @param medicamentoDTO the entity to save.
     * @return the persisted entity.
     */
    MedicamentoDTO save(MedicamentoDTO medicamentoDTO);

    /**
     * Updates a medicamento.
     *
     * @param medicamentoDTO the entity to update.
     * @return the persisted entity.
     */
    MedicamentoDTO update(MedicamentoDTO medicamentoDTO);

    /**
     * Partially updates a medicamento.
     *
     * @param medicamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicamentoDTO> partialUpdate(MedicamentoDTO medicamentoDTO);

    /**
     * Get all the medicamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicamentoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" medicamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicamentoDTO> findOne(Long id);

    /**
     * Delete the "id" medicamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
