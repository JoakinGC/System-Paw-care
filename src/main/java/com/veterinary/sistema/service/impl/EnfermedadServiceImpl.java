package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Enfermedad;
import com.veterinary.sistema.repository.EnfermedadRepository;
import com.veterinary.sistema.service.EnfermedadService;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
import com.veterinary.sistema.service.mapper.EnfermedadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Enfermedad}.
 */
@Service
@Transactional
public class EnfermedadServiceImpl implements EnfermedadService {

    private final Logger log = LoggerFactory.getLogger(EnfermedadServiceImpl.class);

    private final EnfermedadRepository enfermedadRepository;

    private final EnfermedadMapper enfermedadMapper;

    public EnfermedadServiceImpl(EnfermedadRepository enfermedadRepository, EnfermedadMapper enfermedadMapper) {
        this.enfermedadRepository = enfermedadRepository;
        this.enfermedadMapper = enfermedadMapper;
    }

    @Override
    public EnfermedadDTO save(EnfermedadDTO enfermedadDTO) {
        log.debug("Request to save Enfermedad : {}", enfermedadDTO);
        Enfermedad enfermedad = enfermedadMapper.toEntity(enfermedadDTO);
        enfermedad = enfermedadRepository.save(enfermedad);
        return enfermedadMapper.toDto(enfermedad);
    }

    @Override
    public EnfermedadDTO update(EnfermedadDTO enfermedadDTO) {
        log.debug("Request to update Enfermedad : {}", enfermedadDTO);
        Enfermedad enfermedad = enfermedadMapper.toEntity(enfermedadDTO);
        enfermedad = enfermedadRepository.save(enfermedad);
        return enfermedadMapper.toDto(enfermedad);
    }

    @Override
    public Optional<EnfermedadDTO> partialUpdate(EnfermedadDTO enfermedadDTO) {
        log.debug("Request to partially update Enfermedad : {}", enfermedadDTO);

        return enfermedadRepository
            .findById(enfermedadDTO.getId())
            .map(existingEnfermedad -> {
                enfermedadMapper.partialUpdate(existingEnfermedad, enfermedadDTO);

                return existingEnfermedad;
            })
            .map(enfermedadRepository::save)
            .map(enfermedadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnfermedadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enfermedads");
        return enfermedadRepository.findAll(pageable).map(enfermedadMapper::toDto);
    }

    public Page<EnfermedadDTO> findAllWithEagerRelationships(Pageable pageable) {
        return enfermedadRepository.findAllWithEagerRelationships(pageable).map(enfermedadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnfermedadDTO> findOne(Long id) {
        log.debug("Request to get Enfermedad : {}", id);
        return enfermedadRepository.findOneWithEagerRelationships(id).map(enfermedadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Enfermedad : {}", id);
        enfermedadRepository.deleteById(id);
    }
}
