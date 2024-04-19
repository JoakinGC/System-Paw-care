package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Enfermedad;
import com.veterinary.sistema.domain.Especie;
import com.veterinary.sistema.domain.Factores;
import com.veterinary.sistema.domain.Historial;
import com.veterinary.sistema.domain.Raza;
import com.veterinary.sistema.domain.Terapia;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
import com.veterinary.sistema.service.dto.EspecieDTO;
import com.veterinary.sistema.service.dto.FactoresDTO;
import com.veterinary.sistema.service.dto.HistorialDTO;
import com.veterinary.sistema.service.dto.RazaDTO;
import com.veterinary.sistema.service.dto.TerapiaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enfermedad} and its DTO {@link EnfermedadDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnfermedadMapper extends EntityMapper<EnfermedadDTO, Enfermedad> {
    @Mapping(target = "razas", source = "razas", qualifiedByName = "razaIdSet")
    @Mapping(target = "especies", source = "especies", qualifiedByName = "especieIdSet")
    @Mapping(target = "terapias", source = "terapias", qualifiedByName = "terapiaIdSet")
    @Mapping(target = "factores", source = "factores", qualifiedByName = "factoresIdSet")
    @Mapping(target = "historials", source = "historials", qualifiedByName = "historialIdSet")
    EnfermedadDTO toDto(Enfermedad s);

    @Mapping(target = "removeRaza", ignore = true)
    @Mapping(target = "removeEspecie", ignore = true)
    @Mapping(target = "removeTerapia", ignore = true)
    @Mapping(target = "removeFactores", ignore = true)
    @Mapping(target = "historials", ignore = true)
    @Mapping(target = "removeHistorial", ignore = true)
    Enfermedad toEntity(EnfermedadDTO enfermedadDTO);

    @Named("razaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RazaDTO toDtoRazaId(Raza raza);

    @Named("razaIdSet")
    default Set<RazaDTO> toDtoRazaIdSet(Set<Raza> raza) {
        return raza.stream().map(this::toDtoRazaId).collect(Collectors.toSet());
    }

    @Named("especieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EspecieDTO toDtoEspecieId(Especie especie);

    @Named("especieIdSet")
    default Set<EspecieDTO> toDtoEspecieIdSet(Set<Especie> especie) {
        return especie.stream().map(this::toDtoEspecieId).collect(Collectors.toSet());
    }

    @Named("terapiaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TerapiaDTO toDtoTerapiaId(Terapia terapia);

    @Named("terapiaIdSet")
    default Set<TerapiaDTO> toDtoTerapiaIdSet(Set<Terapia> terapia) {
        return terapia.stream().map(this::toDtoTerapiaId).collect(Collectors.toSet());
    }

    @Named("factoresId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FactoresDTO toDtoFactoresId(Factores factores);

    @Named("factoresIdSet")
    default Set<FactoresDTO> toDtoFactoresIdSet(Set<Factores> factores) {
        return factores.stream().map(this::toDtoFactoresId).collect(Collectors.toSet());
    }

    @Named("historialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HistorialDTO toDtoHistorialId(Historial historial);

    @Named("historialIdSet")
    default Set<HistorialDTO> toDtoHistorialIdSet(Set<Historial> historial) {
        return historial.stream().map(this::toDtoHistorialId).collect(Collectors.toSet());
    }
}
