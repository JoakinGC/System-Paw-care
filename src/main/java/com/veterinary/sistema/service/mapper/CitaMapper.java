package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Cita;
import com.veterinary.sistema.domain.CuidadoraHotel;
import com.veterinary.sistema.domain.Estetica;
import com.veterinary.sistema.domain.Mascota;
import com.veterinary.sistema.domain.Veterinario;
import com.veterinary.sistema.service.dto.CitaDTO;
import com.veterinary.sistema.service.dto.CuidadoraHotelDTO;
import com.veterinary.sistema.service.dto.EsteticaDTO;
import com.veterinary.sistema.service.dto.MascotaDTO;
import com.veterinary.sistema.service.dto.VeterinarioDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cita} and its DTO {@link CitaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CitaMapper extends EntityMapper<CitaDTO, Cita> {
    @Mapping(target = "estetica", source = "estetica", qualifiedByName = "esteticaId")
    @Mapping(target = "cuidadoraHotel", source = "cuidadoraHotel", qualifiedByName = "cuidadoraHotelId")
    @Mapping(target = "veterinario", source = "veterinario", qualifiedByName = "veterinarioId")
    @Mapping(target = "mascotas", source = "mascotas", qualifiedByName = "mascotaIdSet")
    CitaDTO toDto(Cita s);

    @Mapping(target = "mascotas", ignore = true)
    @Mapping(target = "removeMascota", ignore = true)
    Cita toEntity(CitaDTO citaDTO);

    @Named("esteticaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EsteticaDTO toDtoEsteticaId(Estetica estetica);

    @Named("cuidadoraHotelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CuidadoraHotelDTO toDtoCuidadoraHotelId(CuidadoraHotel cuidadoraHotel);

    @Named("veterinarioId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VeterinarioDTO toDtoVeterinarioId(Veterinario veterinario);

    @Named("mascotaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MascotaDTO toDtoMascotaId(Mascota mascota);

    @Named("mascotaIdSet")
    default Set<MascotaDTO> toDtoMascotaIdSet(Set<Mascota> mascota) {
        return mascota.stream().map(this::toDtoMascotaId).collect(Collectors.toSet());
    }
}
