package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Factores;
import com.veterinary.sistema.repository.FactoresRepository;
import com.veterinary.sistema.service.FactoresService;
import com.veterinary.sistema.service.dto.FactoresDTO;
import com.veterinary.sistema.service.mapper.FactoresMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Factores}.
 */
@Service
@Transactional
public class FactoresServiceImpl implements FactoresService {

    private final Logger log = LoggerFactory.getLogger(FactoresServiceImpl.class);

    private final FactoresRepository factoresRepository;

    private final FactoresMapper factoresMapper;

    public FactoresServiceImpl(FactoresRepository factoresRepository, FactoresMapper factoresMapper) {
        this.factoresRepository = factoresRepository;
        this.factoresMapper = factoresMapper;
    }

    @Override
    public FactoresDTO save(FactoresDTO factoresDTO) {
        log.debug("Request to save Factores : {}", factoresDTO);
        Factores factores = factoresMapper.toEntity(factoresDTO);
        factores = factoresRepository.save(factores);
        return factoresMapper.toDto(factores);
    }

    @Override
    public FactoresDTO update(FactoresDTO factoresDTO) {
        log.debug("Request to update Factores : {}", factoresDTO);
        Factores factores = factoresMapper.toEntity(factoresDTO);
        factores = factoresRepository.save(factores);
        return factoresMapper.toDto(factores);
    }

    @Override
    public Optional<FactoresDTO> partialUpdate(FactoresDTO factoresDTO) {
        log.debug("Request to partially update Factores : {}", factoresDTO);

        return factoresRepository
            .findById(factoresDTO.getId())
            .map(existingFactores -> {
                factoresMapper.partialUpdate(existingFactores, factoresDTO);

                return existingFactores;
            })
            .map(factoresRepository::save)
            .map(factoresMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FactoresDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Factores");
        return factoresRepository.findAll(pageable).map(factoresMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FactoresDTO> findOne(Long id) {
        log.debug("Request to get Factores : {}", id);
        return factoresRepository.findById(id).map(factoresMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factores : {}", id);
        factoresRepository.deleteById(id);
    }
}
