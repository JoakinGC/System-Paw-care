package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Enfermedad;
import com.veterinary.sistema.domain.Raza;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
import com.veterinary.sistema.service.dto.RazaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Raza} and its DTO {@link RazaDTO}.
 */
@Mapper(componentModel = "spring")
public interface RazaMapper extends EntityMapper<RazaDTO, Raza> {
    @Mapping(target = "enfermedads", source = "enfermedads", qualifiedByName = "enfermedadIdSet")
    RazaDTO toDto(Raza s);

    @Mapping(target = "enfermedads", ignore = true)
    @Mapping(target = "removeEnfermedad", ignore = true)
    Raza toEntity(RazaDTO razaDTO);

    @Named("enfermedadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnfermedadDTO toDtoEnfermedadId(Enfermedad enfermedad);

    @Named("enfermedadIdSet")
    default Set<EnfermedadDTO> toDtoEnfermedadIdSet(Set<Enfermedad> enfermedad) {
        return enfermedad.stream().map(this::toDtoEnfermedadId).collect(Collectors.toSet());
    }
}
