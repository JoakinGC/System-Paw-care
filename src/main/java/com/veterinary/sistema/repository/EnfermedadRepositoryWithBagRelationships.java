package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Enfermedad;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EnfermedadRepositoryWithBagRelationships {
    Optional<Enfermedad> fetchBagRelationships(Optional<Enfermedad> enfermedad);

    List<Enfermedad> fetchBagRelationships(List<Enfermedad> enfermedads);

    Page<Enfermedad> fetchBagRelationships(Page<Enfermedad> enfermedads);
}
