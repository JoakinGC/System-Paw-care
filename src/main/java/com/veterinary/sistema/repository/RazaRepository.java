package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Raza;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Raza entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RazaRepository extends JpaRepository<Raza, Long> {}
