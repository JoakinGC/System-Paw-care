package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Terapia;
import com.veterinary.sistema.repository.TerapiaRepository;
import com.veterinary.sistema.service.TerapiaService;
import com.veterinary.sistema.service.dto.TerapiaDTO;
import com.veterinary.sistema.service.mapper.TerapiaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Terapia}.
 */
@Service
@Transactional
public class TerapiaServiceImpl implements TerapiaService {

    private final Logger log = LoggerFactory.getLogger(TerapiaServiceImpl.class);

    private final TerapiaRepository terapiaRepository;

    private final TerapiaMapper terapiaMapper;

    public TerapiaServiceImpl(TerapiaRepository terapiaRepository, TerapiaMapper terapiaMapper) {
        this.terapiaRepository = terapiaRepository;
        this.terapiaMapper = terapiaMapper;
    }

    @Override
    public TerapiaDTO save(TerapiaDTO terapiaDTO) {
        log.debug("Request to save Terapia : {}", terapiaDTO);
        Terapia terapia = terapiaMapper.toEntity(terapiaDTO);
        terapia = terapiaRepository.save(terapia);
        return terapiaMapper.toDto(terapia);
    }

    @Override
    public TerapiaDTO update(TerapiaDTO terapiaDTO) {
        log.debug("Request to update Terapia : {}", terapiaDTO);
        Terapia terapia = terapiaMapper.toEntity(terapiaDTO);
        terapia = terapiaRepository.save(terapia);
        return terapiaMapper.toDto(terapia);
    }

    @Override
    public Optional<TerapiaDTO> partialUpdate(TerapiaDTO terapiaDTO) {
        log.debug("Request to partially update Terapia : {}", terapiaDTO);

        return terapiaRepository
            .findById(terapiaDTO.getId())
            .map(existingTerapia -> {
                terapiaMapper.partialUpdate(existingTerapia, terapiaDTO);

                return existingTerapia;
            })
            .map(terapiaRepository::save)
            .map(terapiaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TerapiaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Terapias");
        return terapiaRepository.findAll(pageable).map(terapiaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TerapiaDTO> findOne(Long id) {
        log.debug("Request to get Terapia : {}", id);
        return terapiaRepository.findById(id).map(terapiaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Terapia : {}", id);
        terapiaRepository.deleteById(id);
    }
}
