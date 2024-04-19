package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Historial;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface HistorialRepositoryWithBagRelationships {
    Optional<Historial> fetchBagRelationships(Optional<Historial> historial);

    List<Historial> fetchBagRelationships(List<Historial> historials);

    Page<Historial> fetchBagRelationships(Page<Historial> historials);
}
