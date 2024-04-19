package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Mascota;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MascotaRepositoryWithBagRelationships {
    Optional<Mascota> fetchBagRelationships(Optional<Mascota> mascota);

    List<Mascota> fetchBagRelationships(List<Mascota> mascotas);

    Page<Mascota> fetchBagRelationships(Page<Mascota> mascotas);
}
