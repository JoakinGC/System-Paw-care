package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Enfermedad;
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
public class EnfermedadRepositoryWithBagRelationshipsImpl implements EnfermedadRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String ENFERMEDADS_PARAMETER = "enfermedads";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Enfermedad> fetchBagRelationships(Optional<Enfermedad> enfermedad) {
        return enfermedad.map(this::fetchRazas).map(this::fetchEspecies).map(this::fetchTerapias).map(this::fetchFactores);
    }

    @Override
    public Page<Enfermedad> fetchBagRelationships(Page<Enfermedad> enfermedads) {
        return new PageImpl<>(fetchBagRelationships(enfermedads.getContent()), enfermedads.getPageable(), enfermedads.getTotalElements());
    }

    @Override
    public List<Enfermedad> fetchBagRelationships(List<Enfermedad> enfermedads) {
        return Optional.of(enfermedads)
            .map(this::fetchRazas)
            .map(this::fetchEspecies)
            .map(this::fetchTerapias)
            .map(this::fetchFactores)
            .orElse(Collections.emptyList());
    }

    Enfermedad fetchRazas(Enfermedad result) {
        return entityManager
            .createQuery(
                "select enfermedad from Enfermedad enfermedad left join fetch enfermedad.razas where enfermedad.id = :id",
                Enfermedad.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Enfermedad> fetchRazas(List<Enfermedad> enfermedads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, enfermedads.size()).forEach(index -> order.put(enfermedads.get(index).getId(), index));
        List<Enfermedad> result = entityManager
            .createQuery(
                "select enfermedad from Enfermedad enfermedad left join fetch enfermedad.razas where enfermedad in :enfermedads",
                Enfermedad.class
            )
            .setParameter(ENFERMEDADS_PARAMETER, enfermedads)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Enfermedad fetchEspecies(Enfermedad result) {
        return entityManager
            .createQuery(
                "select enfermedad from Enfermedad enfermedad left join fetch enfermedad.especies where enfermedad.id = :id",
                Enfermedad.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Enfermedad> fetchEspecies(List<Enfermedad> enfermedads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, enfermedads.size()).forEach(index -> order.put(enfermedads.get(index).getId(), index));
        List<Enfermedad> result = entityManager
            .createQuery(
                "select enfermedad from Enfermedad enfermedad left join fetch enfermedad.especies where enfermedad in :enfermedads",
                Enfermedad.class
            )
            .setParameter(ENFERMEDADS_PARAMETER, enfermedads)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Enfermedad fetchTerapias(Enfermedad result) {
        return entityManager
            .createQuery(
                "select enfermedad from Enfermedad enfermedad left join fetch enfermedad.terapias where enfermedad.id = :id",
                Enfermedad.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Enfermedad> fetchTerapias(List<Enfermedad> enfermedads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, enfermedads.size()).forEach(index -> order.put(enfermedads.get(index).getId(), index));
        List<Enfermedad> result = entityManager
            .createQuery(
                "select enfermedad from Enfermedad enfermedad left join fetch enfermedad.terapias where enfermedad in :enfermedads",
                Enfermedad.class
            )
            .setParameter(ENFERMEDADS_PARAMETER, enfermedads)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Enfermedad fetchFactores(Enfermedad result) {
        return entityManager
            .createQuery(
                "select enfermedad from Enfermedad enfermedad left join fetch enfermedad.factores where enfermedad.id = :id",
                Enfermedad.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Enfermedad> fetchFactores(List<Enfermedad> enfermedads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, enfermedads.size()).forEach(index -> order.put(enfermedads.get(index).getId(), index));
        List<Enfermedad> result = entityManager
            .createQuery(
                "select enfermedad from Enfermedad enfermedad left join fetch enfermedad.factores where enfermedad in :enfermedads",
                Enfermedad.class
            )
            .setParameter(ENFERMEDADS_PARAMETER, enfermedads)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
