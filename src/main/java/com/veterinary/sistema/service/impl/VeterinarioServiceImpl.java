package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Veterinario;
import com.veterinary.sistema.repository.VeterinarioRepository;
import com.veterinary.sistema.service.VeterinarioService;
import com.veterinary.sistema.service.dto.VeterinarioDTO;
import com.veterinary.sistema.service.mapper.VeterinarioMapper;
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
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Veterinario}.
 */
@Service
@Transactional
public class VeterinarioServiceImpl implements VeterinarioService {

    private final Logger log = LoggerFactory.getLogger(VeterinarioServiceImpl.class);

    private final VeterinarioRepository veterinarioRepository;

    private final VeterinarioMapper veterinarioMapper;

    public VeterinarioServiceImpl(VeterinarioRepository veterinarioRepository, VeterinarioMapper veterinarioMapper) {
        this.veterinarioRepository = veterinarioRepository;
        this.veterinarioMapper = veterinarioMapper;
    }

    @Override
    public VeterinarioDTO save(VeterinarioDTO veterinarioDTO) {
        log.debug("Request to save Veterinario : {}", veterinarioDTO);
        Veterinario veterinario = veterinarioMapper.toEntity(veterinarioDTO);
        veterinario = veterinarioRepository.save(veterinario);
        return veterinarioMapper.toDto(veterinario);
    }

    @Override
    public VeterinarioDTO update(VeterinarioDTO veterinarioDTO) {
        log.debug("Request to update Veterinario : {}", veterinarioDTO);
        Veterinario veterinario = veterinarioMapper.toEntity(veterinarioDTO);
        veterinario = veterinarioRepository.save(veterinario);
        return veterinarioMapper.toDto(veterinario);
    }

    @Override
    public Optional<VeterinarioDTO> partialUpdate(VeterinarioDTO veterinarioDTO) {
        log.debug("Request to partially update Veterinario : {}", veterinarioDTO);

        return veterinarioRepository
            .findById(veterinarioDTO.getId())
            .map(existingVeterinario -> {
                veterinarioMapper.partialUpdate(existingVeterinario, veterinarioDTO);

                return existingVeterinario;
            })
            .map(veterinarioRepository::save)
            .map(veterinarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VeterinarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Veterinarios");
        return veterinarioRepository.findAll(pageable).map(veterinarioMapper::toDto);
    }

    /**
     *  Get all the veterinarios where Usuario is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VeterinarioDTO> findAllWhereUsuarioIsNull() {
        log.debug("Request to get all veterinarios where Usuario is null");
        return StreamSupport.stream(veterinarioRepository.findAll().spliterator(), false)
            .filter(veterinario -> veterinario.getUsuario() == null)
            .map(veterinarioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VeterinarioDTO> findOne(Long id) {
        log.debug("Request to get Veterinario : {}", id);
        return veterinarioRepository.findById(id).map(veterinarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Veterinario : {}", id);
        veterinarioRepository.deleteById(id);
    }
}
