package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Enfermedad;
import com.veterinary.sistema.domain.Factores;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
import com.veterinary.sistema.service.dto.FactoresDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factores} and its DTO {@link FactoresDTO}.
 */
@Mapper(componentModel = "spring")
public interface FactoresMapper extends EntityMapper<FactoresDTO, Factores> {
    @Mapping(target = "enfermedads", source = "enfermedads", qualifiedByName = "enfermedadIdSet")
    FactoresDTO toDto(Factores s);

    @Mapping(target = "enfermedads", ignore = true)
    @Mapping(target = "removeEnfermedad", ignore = true)
    Factores toEntity(FactoresDTO factoresDTO);

    @Named("enfermedadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnfermedadDTO toDtoEnfermedadId(Enfermedad enfermedad);

    @Named("enfermedadIdSet")
    default Set<EnfermedadDTO> toDtoEnfermedadIdSet(Set<Enfermedad> enfermedad) {
        return enfermedad.stream().map(this::toDtoEnfermedadId).collect(Collectors.toSet());
    }
}
