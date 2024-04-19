package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Raza;
import com.veterinary.sistema.repository.RazaRepository;
import com.veterinary.sistema.service.RazaService;
import com.veterinary.sistema.service.dto.RazaDTO;
import com.veterinary.sistema.service.mapper.RazaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Raza}.
 */
@Service
@Transactional
public class RazaServiceImpl implements RazaService {

    private final Logger log = LoggerFactory.getLogger(RazaServiceImpl.class);

    private final RazaRepository razaRepository;

    private final RazaMapper razaMapper;

    public RazaServiceImpl(RazaRepository razaRepository, RazaMapper razaMapper) {
        this.razaRepository = razaRepository;
        this.razaMapper = razaMapper;
    }

    @Override
    public RazaDTO save(RazaDTO razaDTO) {
        log.debug("Request to save Raza : {}", razaDTO);
        Raza raza = razaMapper.toEntity(razaDTO);
        raza = razaRepository.save(raza);
        return razaMapper.toDto(raza);
    }

    @Override
    public RazaDTO update(RazaDTO razaDTO) {
        log.debug("Request to update Raza : {}", razaDTO);
        Raza raza = razaMapper.toEntity(razaDTO);
        raza = razaRepository.save(raza);
        return razaMapper.toDto(raza);
    }

    @Override
    public Optional<RazaDTO> partialUpdate(RazaDTO razaDTO) {
        log.debug("Request to partially update Raza : {}", razaDTO);

        return razaRepository
            .findById(razaDTO.getId())
            .map(existingRaza -> {
                razaMapper.partialUpdate(existingRaza, razaDTO);

                return existingRaza;
            })
            .map(razaRepository::save)
            .map(razaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RazaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Razas");
        return razaRepository.findAll(pageable).map(razaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RazaDTO> findOne(Long id) {
        log.debug("Request to get Raza : {}", id);
        return razaRepository.findById(id).map(razaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Raza : {}", id);
        razaRepository.deleteById(id);
    }
}
