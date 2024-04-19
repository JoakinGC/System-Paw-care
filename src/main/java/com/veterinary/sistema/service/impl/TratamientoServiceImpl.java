package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Tratamiento;
import com.veterinary.sistema.repository.TratamientoRepository;
import com.veterinary.sistema.service.TratamientoService;
import com.veterinary.sistema.service.dto.TratamientoDTO;
import com.veterinary.sistema.service.mapper.TratamientoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Tratamiento}.
 */
@Service
@Transactional
public class TratamientoServiceImpl implements TratamientoService {

    private final Logger log = LoggerFactory.getLogger(TratamientoServiceImpl.class);

    private final TratamientoRepository tratamientoRepository;

    private final TratamientoMapper tratamientoMapper;

    public TratamientoServiceImpl(TratamientoRepository tratamientoRepository, TratamientoMapper tratamientoMapper) {
        this.tratamientoRepository = tratamientoRepository;
        this.tratamientoMapper = tratamientoMapper;
    }

    @Override
    public TratamientoDTO save(TratamientoDTO tratamientoDTO) {
        log.debug("Request to save Tratamiento : {}", tratamientoDTO);
        Tratamiento tratamiento = tratamientoMapper.toEntity(tratamientoDTO);
        tratamiento = tratamientoRepository.save(tratamiento);
        return tratamientoMapper.toDto(tratamiento);
    }

    @Override
    public TratamientoDTO update(TratamientoDTO tratamientoDTO) {
        log.debug("Request to update Tratamiento : {}", tratamientoDTO);
        Tratamiento tratamiento = tratamientoMapper.toEntity(tratamientoDTO);
        tratamiento = tratamientoRepository.save(tratamiento);
        return tratamientoMapper.toDto(tratamiento);
    }

    @Override
    public Optional<TratamientoDTO> partialUpdate(TratamientoDTO tratamientoDTO) {
        log.debug("Request to partially update Tratamiento : {}", tratamientoDTO);

        return tratamientoRepository
            .findById(tratamientoDTO.getId())
            .map(existingTratamiento -> {
                tratamientoMapper.partialUpdate(existingTratamiento, tratamientoDTO);

                return existingTratamiento;
            })
            .map(tratamientoRepository::save)
            .map(tratamientoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TratamientoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tratamientos");
        return tratamientoRepository.findAll(pageable).map(tratamientoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TratamientoDTO> findOne(Long id) {
        log.debug("Request to get Tratamiento : {}", id);
        return tratamientoRepository.findById(id).map(tratamientoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tratamiento : {}", id);
        tratamientoRepository.deleteById(id);
    }
}
