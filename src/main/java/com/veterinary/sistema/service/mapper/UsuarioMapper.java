package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Dueno;
import com.veterinary.sistema.domain.Estetica;
import com.veterinary.sistema.domain.Usuario;
import com.veterinary.sistema.domain.Veterinario;
import com.veterinary.sistema.service.dto.DuenoDTO;
import com.veterinary.sistema.service.dto.EsteticaDTO;
import com.veterinary.sistema.service.dto.UsuarioDTO;
import com.veterinary.sistema.service.dto.VeterinarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Usuario} and its DTO {@link UsuarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario> {
    @Mapping(target = "estetica", source = "estetica", qualifiedByName = "esteticaId")
    @Mapping(target = "veterinario", source = "veterinario", qualifiedByName = "veterinarioId")
    @Mapping(target = "dueno", source = "dueno", qualifiedByName = "duenoId")
    UsuarioDTO toDto(Usuario s);

    @Named("esteticaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EsteticaDTO toDtoEsteticaId(Estetica estetica);

    @Named("veterinarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VeterinarioDTO toDtoVeterinarioId(Veterinario veterinario);

    @Named("duenoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DuenoDTO toDtoDuenoId(Dueno dueno);
}
