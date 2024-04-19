package com.veterinary.sistema.service;

import com.veterinary.sistema.service.dto.CuidadoraHotelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.veterinary.sistema.domain.CuidadoraHotel}.
 */
public interface CuidadoraHotelService {
    /**
     * Save a cuidadoraHotel.
     *
     * @param cuidadoraHotelDTO the entity to save.
     * @return the persisted entity.
     */
    CuidadoraHotelDTO save(CuidadoraHotelDTO cuidadoraHotelDTO);

    /**
     * Updates a cuidadoraHotel.
     *
     * @param cuidadoraHotelDTO the entity to update.
     * @return the persisted entity.
     */
    CuidadoraHotelDTO update(CuidadoraHotelDTO cuidadoraHotelDTO);

    /**
     * Partially updates a cuidadoraHotel.
     *
     * @param cuidadoraHotelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CuidadoraHotelDTO> partialUpdate(CuidadoraHotelDTO cuidadoraHotelDTO);

    /**
     * Get all the cuidadoraHotels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CuidadoraHotelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cuidadoraHotel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CuidadoraHotelDTO> findOne(Long id);

    /**
     * Delete the "id" cuidadoraHotel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
