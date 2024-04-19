package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Estetica;
import com.veterinary.sistema.repository.EsteticaRepository;
import com.veterinary.sistema.service.EsteticaService;
import com.veterinary.sistema.service.dto.EsteticaDTO;
import com.veterinary.sistema.service.mapper.EsteticaMapper;
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
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Estetica}.
 */
@Service
@Transactional
public class EsteticaServiceImpl implements EsteticaService {

    private final Logger log = LoggerFactory.getLogger(EsteticaServiceImpl.class);

    private final EsteticaRepository esteticaRepository;

    private final EsteticaMapper esteticaMapper;

    public EsteticaServiceImpl(EsteticaRepository esteticaRepository, EsteticaMapper esteticaMapper) {
        this.esteticaRepository = esteticaRepository;
        this.esteticaMapper = esteticaMapper;
    }

    @Override
    public EsteticaDTO save(EsteticaDTO esteticaDTO) {
        log.debug("Request to save Estetica : {}", esteticaDTO);
        Estetica estetica = esteticaMapper.toEntity(esteticaDTO);
        estetica = esteticaRepository.save(estetica);
        return esteticaMapper.toDto(estetica);
    }

    @Override
    public EsteticaDTO update(EsteticaDTO esteticaDTO) {
        log.debug("Request to update Estetica : {}", esteticaDTO);
        Estetica estetica = esteticaMapper.toEntity(esteticaDTO);
        estetica = esteticaRepository.save(estetica);
        return esteticaMapper.toDto(estetica);
    }

    @Override
    public Optional<EsteticaDTO> partialUpdate(EsteticaDTO esteticaDTO) {
        log.debug("Request to partially update Estetica : {}", esteticaDTO);

        return esteticaRepository
            .findById(esteticaDTO.getId())
            .map(existingEstetica -> {
                esteticaMapper.partialUpdate(existingEstetica, esteticaDTO);

                return existingEstetica;
            })
            .map(esteticaRepository::save)
            .map(esteticaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EsteticaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Esteticas");
        return esteticaRepository.findAll(pageable).map(esteticaMapper::toDto);
    }

    /**
     *  Get all the esteticas where Usuario is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EsteticaDTO> findAllWhereUsuarioIsNull() {
        log.debug("Request to get all esteticas where Usuario is null");
        return StreamSupport.stream(esteticaRepository.findAll().spliterator(), false)
            .filter(estetica -> estetica.getUsuario() == null)
            .map(esteticaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EsteticaDTO> findOne(Long id) {
        log.debug("Request to get Estetica : {}", id);
        return esteticaRepository.findById(id).map(esteticaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Estetica : {}", id);
        esteticaRepository.deleteById(id);
    }
}
