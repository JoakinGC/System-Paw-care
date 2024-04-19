package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Dueno;
import com.veterinary.sistema.repository.DuenoRepository;
import com.veterinary.sistema.service.DuenoService;
import com.veterinary.sistema.service.dto.DuenoDTO;
import com.veterinary.sistema.service.mapper.DuenoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Dueno}.
 */
@Service
@Transactional
public class DuenoServiceImpl implements DuenoService {

    private final Logger log = LoggerFactory.getLogger(DuenoServiceImpl.class);

    private final DuenoRepository duenoRepository;

    private final DuenoMapper duenoMapper;

    public DuenoServiceImpl(DuenoRepository duenoRepository, DuenoMapper duenoMapper) {
        this.duenoRepository = duenoRepository;
        this.duenoMapper = duenoMapper;
    }

    @Override
    public DuenoDTO save(DuenoDTO duenoDTO) {
        log.debug("Request to save Dueno : {}", duenoDTO);
        Dueno dueno = duenoMapper.toEntity(duenoDTO);
        dueno = duenoRepository.save(dueno);
        return duenoMapper.toDto(dueno);
    }

    @Override
    public DuenoDTO update(DuenoDTO duenoDTO) {
        log.debug("Request to update Dueno : {}", duenoDTO);
        Dueno dueno = duenoMapper.toEntity(duenoDTO);
        dueno = duenoRepository.save(dueno);
        return duenoMapper.toDto(dueno);
    }

    @Override
    public Optional<DuenoDTO> partialUpdate(DuenoDTO duenoDTO) {
        log.debug("Request to partially update Dueno : {}", duenoDTO);

        return duenoRepository
            .findById(duenoDTO.getId())
            .map(existingDueno -> {
                duenoMapper.partialUpdate(existingDueno, duenoDTO);

                return existingDueno;
            })
            .map(duenoRepository::save)
            .map(duenoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DuenoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Duenos");
        return duenoRepository.findAll(pageable).map(duenoMapper::toDto);
    }

    /**
     *  Get all the duenos where Usuario is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DuenoDTO> findAllWhereUsuarioIsNull() {
        log.debug("Request to get all duenos where Usuario is null");
        return StreamSupport.stream(duenoRepository.findAll().spliterator(), false)
            .filter(dueno -> dueno.getUsuario() == null)
            .map(duenoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DuenoDTO> findOne(Long id) {
        log.debug("Request to get Dueno : {}", id);
        return duenoRepository.findById(id).map(duenoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dueno : {}", id);
        duenoRepository.deleteById(id);
    }
}
