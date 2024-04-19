package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Factores;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Factores entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactoresRepository extends JpaRepository<Factores, Long> {}
