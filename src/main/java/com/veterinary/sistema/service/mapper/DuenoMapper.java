package com.veterinary.sistema.service.mapper;

import com.veterinary.sistema.domain.Dueno;
import com.veterinary.sistema.service.dto.DuenoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dueno} and its DTO {@link DuenoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DuenoMapper extends EntityMapper<DuenoDTO, Dueno> {}
