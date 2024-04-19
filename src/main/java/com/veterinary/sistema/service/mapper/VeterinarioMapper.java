package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Estudios;
import com.veterinary.sistema.domain.Veterinario;
import com.veterinary.sistema.service.dto.EstudiosDTO;
import com.veterinary.sistema.service.dto.VeterinarioDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Veterinario} and its DTO {@link VeterinarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface VeterinarioMapper extends EntityMapper<VeterinarioDTO, Veterinario> {
    @Mapping(target = "estudios", source = "estudios", qualifiedByName = "estudiosIdSet")
    VeterinarioDTO toDto(Veterinario s);

    @Mapping(target = "estudios", ignore = true)
    @Mapping(target = "removeEstudios", ignore = true)
    Veterinario toEntity(VeterinarioDTO veterinarioDTO);

    @Named("estudiosId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EstudiosDTO toDtoEstudiosId(Estudios estudios);

    @Named("estudiosIdSet")
    default Set<EstudiosDTO> toDtoEstudiosIdSet(Set<Estudios> estudios) {
        return estudios.stream().map(this::toDtoEstudiosId).collect(Collectors.toSet());
    }
}
