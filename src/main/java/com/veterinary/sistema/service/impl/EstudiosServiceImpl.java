package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Estudios;
import com.veterinary.sistema.repository.EstudiosRepository;
import com.veterinary.sistema.service.EstudiosService;
import com.veterinary.sistema.service.dto.EstudiosDTO;
import com.veterinary.sistema.service.mapper.EstudiosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Estudios}.
 */
@Service
@Transactional
public class EstudiosServiceImpl implements EstudiosService {

    private final Logger log = LoggerFactory.getLogger(EstudiosServiceImpl.class);

    private final EstudiosRepository estudiosRepository;

    private final EstudiosMapper estudiosMapper;

    public EstudiosServiceImpl(EstudiosRepository estudiosRepository, EstudiosMapper estudiosMapper) {
        this.estudiosRepository = estudiosRepository;
        this.estudiosMapper = estudiosMapper;
    }

    @Override
    public EstudiosDTO save(EstudiosDTO estudiosDTO) {
        log.debug("Request to save Estudios : {}", estudiosDTO);
        Estudios estudios = estudiosMapper.toEntity(estudiosDTO);
        estudios = estudiosRepository.save(estudios);
        return estudiosMapper.toDto(estudios);
    }

    @Override
    public EstudiosDTO update(EstudiosDTO estudiosDTO) {
        log.debug("Request to update Estudios : {}", estudiosDTO);
        Estudios estudios = estudiosMapper.toEntity(estudiosDTO);
        estudios = estudiosRepository.save(estudios);
        return estudiosMapper.toDto(estudios);
    }

    @Override
    public Optional<EstudiosDTO> partialUpdate(EstudiosDTO estudiosDTO) {
        log.debug("Request to partially update Estudios : {}", estudiosDTO);

        return estudiosRepository
            .findById(estudiosDTO.getId())
            .map(existingEstudios -> {
                estudiosMapper.partialUpdate(existingEstudios, estudiosDTO);

                return existingEstudios;
            })
            .map(estudiosRepository::save)
            .map(estudiosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstudiosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Estudios");
        return estudiosRepository.findAll(pageable).map(estudiosMapper::toDto);
    }

    public Page<EstudiosDTO> findAllWithEagerRelationships(Pageable pageable) {
        return estudiosRepository.findAllWithEagerRelationships(pageable).map(estudiosMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstudiosDTO> findOne(Long id) {
        log.debug("Request to get Estudios : {}", id);
        return estudiosRepository.findOneWithEagerRelationships(id).map(estudiosMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Estudios : {}", id);
        estudiosRepository.deleteById(id);
    }
}
