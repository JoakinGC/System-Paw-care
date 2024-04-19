package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.CuidadoraHotel;
import com.veterinary.sistema.service.dto.CuidadoraHotelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CuidadoraHotel} and its DTO {@link CuidadoraHotelDTO}.
 */
@Mapper(componentModel = "spring")
public interface CuidadoraHotelMapper extends EntityMapper<CuidadoraHotelDTO, CuidadoraHotel> {}
