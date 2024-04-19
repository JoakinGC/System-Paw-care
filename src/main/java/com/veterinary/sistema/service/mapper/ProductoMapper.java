package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Producto;
import com.veterinary.sistema.service.dto.ProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {}
