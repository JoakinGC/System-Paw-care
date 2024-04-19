package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Cita;
import com.veterinary.sistema.domain.Dueno;
import com.veterinary.sistema.domain.Especie;
import com.veterinary.sistema.domain.Mascota;
import com.veterinary.sistema.domain.Raza;
import com.veterinary.sistema.service.dto.CitaDTO;
import com.veterinary.sistema.service.dto.DuenoDTO;
import com.veterinary.sistema.service.dto.EspecieDTO;
import com.veterinary.sistema.service.dto.MascotaDTO;
import com.veterinary.sistema.service.dto.RazaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mascota} and its DTO {@link MascotaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MascotaMapper extends EntityMapper<MascotaDTO, Mascota> {
    @Mapping(target = "citas", source = "citas", qualifiedByName = "citaIdSet")
    @Mapping(target = "dueno", source = "dueno", qualifiedByName = "duenoId")
    @Mapping(target = "especie", source = "especie", qualifiedByName = "especieId")
    @Mapping(target = "raza", source = "raza", qualifiedByName = "razaId")
    MascotaDTO toDto(Mascota s);

    @Mapping(target = "removeCita", ignore = true)
    Mascota toEntity(MascotaDTO mascotaDTO);

    @Named("citaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitaDTO toDtoCitaId(Cita cita);

    @Named("citaIdSet")
    default Set<CitaDTO> toDtoCitaIdSet(Set<Cita> cita) {
        return cita.stream().map(this::toDtoCitaId).collect(Collectors.toSet());
    }

    @Named("duenoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DuenoDTO toDtoDuenoId(Dueno dueno);

    @Named("especieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EspecieDTO toDtoEspecieId(Especie especie);

    @Named("razaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RazaDTO toDtoRazaId(Raza raza);
}
