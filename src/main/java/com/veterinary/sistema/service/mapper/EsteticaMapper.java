package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Estetica;
import com.veterinary.sistema.service.dto.EsteticaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Estetica} and its DTO {@link EsteticaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EsteticaMapper extends EntityMapper<EsteticaDTO, Estetica> {}
