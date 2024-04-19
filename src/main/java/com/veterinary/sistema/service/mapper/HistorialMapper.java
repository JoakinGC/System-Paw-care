package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Enfermedad;
import com.veterinary.sistema.domain.Historial;
import com.veterinary.sistema.domain.Mascota;
import com.veterinary.sistema.domain.Medicamento;
import com.veterinary.sistema.domain.Veterinario;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
import com.veterinary.sistema.service.dto.HistorialDTO;
import com.veterinary.sistema.service.dto.MascotaDTO;
import com.veterinary.sistema.service.dto.MedicamentoDTO;
import com.veterinary.sistema.service.dto.VeterinarioDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Historial} and its DTO {@link HistorialDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistorialMapper extends EntityMapper<HistorialDTO, Historial> {
    @Mapping(target = "medicamentos", source = "medicamentos", qualifiedByName = "medicamentoIdSet")
    @Mapping(target = "enfermedads", source = "enfermedads", qualifiedByName = "enfermedadIdSet")
    @Mapping(target = "veterinario", source = "veterinario", qualifiedByName = "veterinarioId")
    @Mapping(target = "mascota", source = "mascota", qualifiedByName = "mascotaId")
    HistorialDTO toDto(Historial s);

    @Mapping(target = "removeMedicamento", ignore = true)
    @Mapping(target = "removeEnfermedad", ignore = true)
    Historial toEntity(HistorialDTO historialDTO);

    @Named("medicamentoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicamentoDTO toDtoMedicamentoId(Medicamento medicamento);

    @Named("medicamentoIdSet")
    default Set<MedicamentoDTO> toDtoMedicamentoIdSet(Set<Medicamento> medicamento) {
        return medicamento.stream().map(this::toDtoMedicamentoId).collect(Collectors.toSet());
    }

    @Named("enfermedadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnfermedadDTO toDtoEnfermedadId(Enfermedad enfermedad);

    @Named("enfermedadIdSet")
    default Set<EnfermedadDTO> toDtoEnfermedadIdSet(Set<Enfermedad> enfermedad) {
        return enfermedad.stream().map(this::toDtoEnfermedadId).collect(Collectors.toSet());
    }

    @Named("veterinarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VeterinarioDTO toDtoVeterinarioId(Veterinario veterinario);

    @Named("mascotaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MascotaDTO toDtoMascotaId(Mascota mascota);
}
