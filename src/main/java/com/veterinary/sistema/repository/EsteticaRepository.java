package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Estetica;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Estetica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EsteticaRepository extends JpaRepository<Estetica, Long> {}
