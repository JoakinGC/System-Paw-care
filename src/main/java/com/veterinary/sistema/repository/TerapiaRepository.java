package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Terapia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Terapia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TerapiaRepository extends JpaRepository<Terapia, Long> {}
