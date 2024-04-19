package com.veterinary.sistema.repository;

import com.veterinary.sistema.domain.Mascota;
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
public class MascotaRepositoryWithBagRelationshipsImpl implements MascotaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String MASCOTAS_PARAMETER = "mascotas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Mascota> fetchBagRelationships(Optional<Mascota> mascota) {
        return mascota.map(this::fetchCitas);
    }

    @Override
    public Page<Mascota> fetchBagRelationships(Page<Mascota> mascotas) {
        return new PageImpl<>(fetchBagRelationships(mascotas.getContent()), mascotas.getPageable(), mascotas.getTotalElements());
    }

    @Override
    public List<Mascota> fetchBagRelationships(List<Mascota> mascotas) {
        return Optional.of(mascotas).map(this::fetchCitas).orElse(Collections.emptyList());
    }

    Mascota fetchCitas(Mascota result) {
        return entityManager
            .createQuery("select mascota from Mascota mascota left join fetch mascota.citas where mascota.id = :id", Mascota.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Mascota> fetchCitas(List<Mascota> mascotas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, mascotas.size()).forEach(index -> order.put(mascotas.get(index).getId(), index));
        List<Mascota> result = entityManager
            .createQuery("select mascota from Mascota mascota left join fetch mascota.citas where mascota in :mascotas", Mascota.class)
            .setParameter(MASCOTAS_PARAMETER, mascotas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
