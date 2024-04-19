package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Historial;
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
public class HistorialRepositoryWithBagRelationshipsImpl implements HistorialRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String HISTORIALS_PARAMETER = "historials";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Historial> fetchBagRelationships(Optional<Historial> historial) {
        return historial.map(this::fetchMedicamentos).map(this::fetchEnfermedads);
    }

    @Override
    public Page<Historial> fetchBagRelationships(Page<Historial> historials) {
        return new PageImpl<>(fetchBagRelationships(historials.getContent()), historials.getPageable(), historials.getTotalElements());
    }

    @Override
    public List<Historial> fetchBagRelationships(List<Historial> historials) {
        return Optional.of(historials).map(this::fetchMedicamentos).map(this::fetchEnfermedads).orElse(Collections.emptyList());
    }

    Historial fetchMedicamentos(Historial result) {
        return entityManager
            .createQuery(
                "select historial from Historial historial left join fetch historial.medicamentos where historial.id = :id",
                Historial.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Historial> fetchMedicamentos(List<Historial> historials) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, historials.size()).forEach(index -> order.put(historials.get(index).getId(), index));
        List<Historial> result = entityManager
            .createQuery(
                "select historial from Historial historial left join fetch historial.medicamentos where historial in :historials",
                Historial.class
            )
            .setParameter(HISTORIALS_PARAMETER, historials)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Historial fetchEnfermedads(Historial result) {
        return entityManager
            .createQuery(
                "select historial from Historial historial left join fetch historial.enfermedads where historial.id = :id",
                Historial.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Historial> fetchEnfermedads(List<Historial> historials) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, historials.size()).forEach(index -> order.put(historials.get(index).getId(), index));
        List<Historial> result = entityManager
            .createQuery(
                "select historial from Historial historial left join fetch historial.enfermedads where historial in :historials",
                Historial.class
            )
            .setParameter(HISTORIALS_PARAMETER, historials)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
