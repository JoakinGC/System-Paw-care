package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Estudios;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EstudiosRepositoryWithBagRelationshipsImpl implements EstudiosRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ESTUDIOS_PARAMETER = "estudios";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Estudios> fetchBagRelationships(Optional<Estudios> estudios) {
        return estudios.map(this::fetchVeterinarios);
    }

    @Override
    public Page<Estudios> fetchBagRelationships(Page<Estudios> estudios) {
        return new PageImpl<>(fetchBagRelationships(estudios.getContent()), estudios.getPageable(), estudios.getTotalElements());
    }

    @Override
    public List<Estudios> fetchBagRelationships(List<Estudios> estudios) {
        return Optional.of(estudios).map(this::fetchVeterinarios).orElse(Collections.emptyList());
    }

    Estudios fetchVeterinarios(Estudios result) {
        return entityManager
            .createQuery(
                "select estudios from Estudios estudios left join fetch estudios.veterinarios where estudios.id = :id",
                Estudios.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Estudios> fetchVeterinarios(List<Estudios> estudios) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, estudios.size()).forEach(index -> order.put(estudios.get(index).getId(), index));
        List<Estudios> result = entityManager
            .createQuery(
                "select estudios from Estudios estudios left join fetch estudios.veterinarios where estudios in :estudios",
                Estudios.class
            )
            .setParameter(ESTUDIOS_PARAMETER, estudios)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
