package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Estudios;
import com.veterinary.sistema.domain.Veterinario;
import com.veterinary.sistema.service.dto.EstudiosDTO;
import com.veterinary.sistema.service.dto.VeterinarioDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Estudios} and its DTO {@link EstudiosDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstudiosMapper extends EntityMapper<EstudiosDTO, Estudios> {
    @Mapping(target = "veterinarios", source = "veterinarios", qualifiedByName = "veterinarioIdSet")
    EstudiosDTO toDto(Estudios s);

    @Mapping(target = "removeVeterinario", ignore = true)
    Estudios toEntity(EstudiosDTO estudiosDTO);

    @Named("veterinarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VeterinarioDTO toDtoVeterinarioId(Veterinario veterinario);

    @Named("veterinarioIdSet")
    default Set<VeterinarioDTO> toDtoVeterinarioIdSet(Set<Veterinario> veterinario) {
        return veterinario.stream().map(this::toDtoVeterinarioId).collect(Collectors.toSet());
    }
}
