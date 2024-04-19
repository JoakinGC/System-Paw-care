package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Historial;
import com.veterinary.sistema.domain.Tratamiento;
import com.veterinary.sistema.service.dto.HistorialDTO;
import com.veterinary.sistema.service.dto.TratamientoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tratamiento} and its DTO {@link TratamientoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TratamientoMapper extends EntityMapper<TratamientoDTO, Tratamiento> {
    @Mapping(target = "historial", source = "historial", qualifiedByName = "historialId")
    TratamientoDTO toDto(Tratamiento s);

    @Named("historialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HistorialDTO toDtoHistorialId(Historial historial);
}
