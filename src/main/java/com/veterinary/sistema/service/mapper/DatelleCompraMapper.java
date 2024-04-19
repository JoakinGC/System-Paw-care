package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Compra;
import com.veterinary.sistema.domain.DatelleCompra;
import com.veterinary.sistema.domain.Producto;
import com.veterinary.sistema.service.dto.CompraDTO;
import com.veterinary.sistema.service.dto.DatelleCompraDTO;
import com.veterinary.sistema.service.dto.ProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DatelleCompra} and its DTO {@link DatelleCompraDTO}.
 */
@Mapper(componentModel = "spring")
public interface DatelleCompraMapper extends EntityMapper<DatelleCompraDTO, DatelleCompra> {
    @Mapping(target = "compra", source = "compra", qualifiedByName = "compraId")
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoId")
    DatelleCompraDTO toDto(DatelleCompra s);

    @Named("compraId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompraDTO toDtoCompraId(Compra compra);

    @Named("productoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductoDTO toDtoProductoId(Producto producto);
}
