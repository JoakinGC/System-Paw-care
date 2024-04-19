package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Mascota;
import com.veterinary.sistema.repository.MascotaRepository;
import com.veterinary.sistema.service.MascotaService;
import com.veterinary.sistema.service.dto.MascotaDTO;
import com.veterinary.sistema.service.mapper.MascotaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Mascota}.
 */
@Service
@Transactional
public class MascotaServiceImpl implements MascotaService {

    private final Logger log = LoggerFactory.getLogger(MascotaServiceImpl.class);

    private final MascotaRepository mascotaRepository;

    private final MascotaMapper mascotaMapper;

    public MascotaServiceImpl(MascotaRepository mascotaRepository, MascotaMapper mascotaMapper) {
        this.mascotaRepository = mascotaRepository;
        this.mascotaMapper = mascotaMapper;
    }

    @Override
    public MascotaDTO save(MascotaDTO mascotaDTO) {
        log.debug("Request to save Mascota : {}", mascotaDTO);
        Mascota mascota = mascotaMapper.toEntity(mascotaDTO);
        mascota = mascotaRepository.save(mascota);
        return mascotaMapper.toDto(mascota);
    }

    @Override
    public MascotaDTO update(MascotaDTO mascotaDTO) {
        log.debug("Request to update Mascota : {}", mascotaDTO);
        Mascota mascota = mascotaMapper.toEntity(mascotaDTO);
        mascota = mascotaRepository.save(mascota);
        return mascotaMapper.toDto(mascota);
    }

    @Override
    public Optional<MascotaDTO> partialUpdate(MascotaDTO mascotaDTO) {
        log.debug("Request to partially update Mascota : {}", mascotaDTO);

        return mascotaRepository
            .findById(mascotaDTO.getId())
            .map(existingMascota -> {
                mascotaMapper.partialUpdate(existingMascota, mascotaDTO);

                return existingMascota;
            })
            .map(mascotaRepository::save)
            .map(mascotaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MascotaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Mascotas");
        return mascotaRepository.findAll(pageable).map(mascotaMapper::toDto);
    }

    public Page<MascotaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return mascotaRepository.findAllWithEagerRelationships(pageable).map(mascotaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MascotaDTO> findOne(Long id) {
        log.debug("Request to get Mascota : {}", id);
        return mascotaRepository.findOneWithEagerRelationships(id).map(mascotaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Mascota : {}", id);
        mascotaRepository.deleteById(id);
    }
}
