package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Enfermedad;
import com.veterinary.sistema.domain.Terapia;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
import com.veterinary.sistema.service.dto.TerapiaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Terapia} and its DTO {@link TerapiaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TerapiaMapper extends EntityMapper<TerapiaDTO, Terapia> {
    @Mapping(target = "enfermedads", source = "enfermedads", qualifiedByName = "enfermedadIdSet")
    TerapiaDTO toDto(Terapia s);

    @Mapping(target = "enfermedads", ignore = true)
    @Mapping(target = "removeEnfermedad", ignore = true)
    Terapia toEntity(TerapiaDTO terapiaDTO);

    @Named("enfermedadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnfermedadDTO toDtoEnfermedadId(Enfermedad enfermedad);

    @Named("enfermedadIdSet")
    default Set<EnfermedadDTO> toDtoEnfermedadIdSet(Set<Enfermedad> enfermedad) {
        return enfermedad.stream().map(this::toDtoEnfermedadId).collect(Collectors.toSet());
    }
}
