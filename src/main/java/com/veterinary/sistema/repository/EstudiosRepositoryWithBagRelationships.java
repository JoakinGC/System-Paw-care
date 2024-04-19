package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Estudios;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface EstudiosRepositoryWithBagRelationships {
    Optional<Estudios> fetchBagRelationships(Optional<Estudios> estudios);

    List<Estudios> fetchBagRelationships(List<Estudios> estudios);

    Page<Estudios> fetchBagRelationships(Page<Estudios> estudios);
}
