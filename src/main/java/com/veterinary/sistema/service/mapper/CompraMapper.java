package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Compra;
import com.veterinary.sistema.domain.Usuario;
import com.veterinary.sistema.service.dto.CompraDTO;
import com.veterinary.sistema.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compra} and its DTO {@link CompraDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompraMapper extends EntityMapper<CompraDTO, Compra> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioId")
    CompraDTO toDto(Compra s);

    @Named("usuarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsuarioDTO toDtoUsuarioId(Usuario usuario);
}
