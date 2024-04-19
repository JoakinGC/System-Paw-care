package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Veterinario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Veterinario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {}
