package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Enfermedad;
import com.veterinary.sistema.domain.Especie;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
import com.veterinary.sistema.service.dto.EspecieDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Especie} and its DTO {@link EspecieDTO}.
 */
@Mapper(componentModel = "spring")
public interface EspecieMapper extends EntityMapper<EspecieDTO, Especie> {
    @Mapping(target = "enfermedads", source = "enfermedads", qualifiedByName = "enfermedadIdSet")
    EspecieDTO toDto(Especie s);

    @Mapping(target = "enfermedads", ignore = true)
    @Mapping(target = "removeEnfermedad", ignore = true)
    Especie toEntity(EspecieDTO especieDTO);

    @Named("enfermedadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnfermedadDTO toDtoEnfermedadId(Enfermedad enfermedad);

    @Named("enfermedadIdSet")
    default Set<EnfermedadDTO> toDtoEnfermedadIdSet(Set<Enfermedad> enfermedad) {
        return enfermedad.stream().map(this::toDtoEnfermedadId).collect(Collectors.toSet());
    }
}
