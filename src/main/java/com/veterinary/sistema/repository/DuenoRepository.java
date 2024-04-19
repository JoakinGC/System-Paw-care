package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Dueno;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dueno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DuenoRepository extends JpaRepository<Dueno, Long> {}
