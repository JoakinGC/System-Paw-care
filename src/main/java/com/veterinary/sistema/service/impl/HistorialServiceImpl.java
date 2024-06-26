package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Historial;
import com.veterinary.sistema.repository.HistorialRepository;
import com.veterinary.sistema.service.HistorialService;
import com.veterinary.sistema.service.dto.HistorialDTO;
import com.veterinary.sistema.service.mapper.HistorialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Historial}.
 */
@Service
@Transactional
public class HistorialServiceImpl implements HistorialService {

    private final Logger log = LoggerFactory.getLogger(HistorialServiceImpl.class);

    private final HistorialRepository historialRepository;

    private final HistorialMapper historialMapper;

    public HistorialServiceImpl(HistorialRepository historialRepository, HistorialMapper historialMapper) {
        this.historialRepository = historialRepository;
        this.historialMapper = historialMapper;
    }

    @Override
    public HistorialDTO save(HistorialDTO historialDTO) {
        log.debug("Request to save Historial : {}", historialDTO);
        Historial historial = historialMapper.toEntity(historialDTO);
        historial = historialRepository.save(historial);
        return historialMapper.toDto(historial);
    }

    @Override
    public HistorialDTO update(HistorialDTO historialDTO) {
        log.debug("Request to update Historial : {}", historialDTO);
        Historial historial = historialMapper.toEntity(historialDTO);
        historial = historialRepository.save(historial);
        return historialMapper.toDto(historial);
    }

    @Override
    public Optional<HistorialDTO> partialUpdate(HistorialDTO historialDTO) {
        log.debug("Request to partially update Historial : {}", historialDTO);

        return historialRepository
            .findById(historialDTO.getId())
            .map(existingHistorial -> {
                historialMapper.partialUpdate(existingHistorial, historialDTO);

                return existingHistorial;
            })
            .map(historialRepository::save)
            .map(historialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HistorialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Historials");
        return historialRepository.findAll(pageable).map(historialMapper::toDto);
    }

    public Page<HistorialDTO> findAllWithEagerRelationships(Pageable pageable) {
        return historialRepository.findAllWithEagerRelationships(pageable).map(historialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialDTO> findOne(Long id) {
        log.debug("Request to get Historial : {}", id);
        return historialRepository.findOneWithEagerRelationships(id).map(historialMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Historial : {}", id);
        historialRepository.deleteById(id);
    }
}
