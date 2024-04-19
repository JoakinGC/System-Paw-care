package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.CuidadoraHotel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CuidadoraHotel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuidadoraHotelRepository extends JpaRepository<CuidadoraHotel, Long> {}
