package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Especie;
import com.veterinary.sistema.repository.EspecieRepository;
import com.veterinary.sistema.service.EspecieService;
import com.veterinary.sistema.service.dto.EspecieDTO;
import com.veterinary.sistema.service.mapper.EspecieMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Especie}.
 */
@Service
@Transactional
public class EspecieServiceImpl implements EspecieService {

    private final Logger log = LoggerFactory.getLogger(EspecieServiceImpl.class);

    private final EspecieRepository especieRepository;

    private final EspecieMapper especieMapper;

    public EspecieServiceImpl(EspecieRepository especieRepository, EspecieMapper especieMapper) {
        this.especieRepository = especieRepository;
        this.especieMapper = especieMapper;
    }

    @Override
    public EspecieDTO save(EspecieDTO especieDTO) {
        log.debug("Request to save Especie : {}", especieDTO);
        Especie especie = especieMapper.toEntity(especieDTO);
        especie = especieRepository.save(especie);
        return especieMapper.toDto(especie);
    }

    @Override
    public EspecieDTO update(EspecieDTO especieDTO) {
        log.debug("Request to update Especie : {}", especieDTO);
        Especie especie = especieMapper.toEntity(especieDTO);
        especie = especieRepository.save(especie);
        return especieMapper.toDto(especie);
    }

    @Override
    public Optional<EspecieDTO> partialUpdate(EspecieDTO especieDTO) {
        log.debug("Request to partially update Especie : {}", especieDTO);

        return especieRepository
            .findById(especieDTO.getId())
            .map(existingEspecie -> {
                especieMapper.partialUpdate(existingEspecie, especieDTO);

                return existingEspecie;
            })
            .map(especieRepository::save)
            .map(especieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EspecieDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Especies");
        return especieRepository.findAll(pageable).map(especieMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EspecieDTO> findOne(Long id) {
        log.debug("Request to get Especie : {}", id);
        return especieRepository.findById(id).map(especieMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Especie : {}", id);
        especieRepository.deleteById(id);
    }
}
