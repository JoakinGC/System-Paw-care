package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.DatelleCompra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DatelleCompra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DatelleCompraRepository extends JpaRepository<DatelleCompra, Long> {}
