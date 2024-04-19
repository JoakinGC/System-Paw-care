package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Historial;
import com.veterinary.sistema.domain.Medicamento;
import com.veterinary.sistema.service.dto.HistorialDTO;
import com.veterinary.sistema.service.dto.MedicamentoDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicamento} and its DTO {@link MedicamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicamentoMapper extends EntityMapper<MedicamentoDTO, Medicamento> {
    @Mapping(target = "historials", source = "historials", qualifiedByName = "historialIdSet")
    MedicamentoDTO toDto(Medicamento s);

    @Mapping(target = "historials", ignore = true)
    @Mapping(target = "removeHistorial", ignore = true)
    Medicamento toEntity(MedicamentoDTO medicamentoDTO);

    @Named("historialId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HistorialDTO toDtoHistorialId(Historial historial);

    @Named("historialIdSet")
    default Set<HistorialDTO> toDtoHistorialIdSet(Set<Historial> historial) {
        return historial.stream().map(this::toDtoHistorialId).collect(Collectors.toSet());
    }
}
