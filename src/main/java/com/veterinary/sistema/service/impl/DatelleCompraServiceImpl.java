package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.DatelleCompra;
import com.veterinary.sistema.repository.DatelleCompraRepository;
import com.veterinary.sistema.service.DatelleCompraService;
import com.veterinary.sistema.service.dto.DatelleCompraDTO;
import com.veterinary.sistema.service.mapper.DatelleCompraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.DatelleCompra}.
 */
@Service
@Transactional
public class DatelleCompraServiceImpl implements DatelleCompraService {

    private final Logger log = LoggerFactory.getLogger(DatelleCompraServiceImpl.class);

    private final DatelleCompraRepository datelleCompraRepository;

    private final DatelleCompraMapper datelleCompraMapper;

    public DatelleCompraServiceImpl(DatelleCompraRepository datelleCompraRepository, DatelleCompraMapper datelleCompraMapper) {
        this.datelleCompraRepository = datelleCompraRepository;
        this.datelleCompraMapper = datelleCompraMapper;
    }

    @Override
    public DatelleCompraDTO save(DatelleCompraDTO datelleCompraDTO) {
        log.debug("Request to save DatelleCompra : {}", datelleCompraDTO);
        DatelleCompra datelleCompra = datelleCompraMapper.toEntity(datelleCompraDTO);
        datelleCompra = datelleCompraRepository.save(datelleCompra);
        return datelleCompraMapper.toDto(datelleCompra);
    }

    @Override
    public DatelleCompraDTO update(DatelleCompraDTO datelleCompraDTO) {
        log.debug("Request to update DatelleCompra : {}", datelleCompraDTO);
        DatelleCompra datelleCompra = datelleCompraMapper.toEntity(datelleCompraDTO);
        datelleCompra = datelleCompraRepository.save(datelleCompra);
        return datelleCompraMapper.toDto(datelleCompra);
    }

    @Override
    public Optional<DatelleCompraDTO> partialUpdate(DatelleCompraDTO datelleCompraDTO) {
        log.debug("Request to partially update DatelleCompra : {}", datelleCompraDTO);

        return datelleCompraRepository
            .findById(datelleCompraDTO.getId())
            .map(existingDatelleCompra -> {
                datelleCompraMapper.partialUpdate(existingDatelleCompra, datelleCompraDTO);

                return existingDatelleCompra;
            })
            .map(datelleCompraRepository::save)
            .map(datelleCompraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DatelleCompraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DatelleCompras");
        return datelleCompraRepository.findAll(pageable).map(datelleCompraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DatelleCompraDTO> findOne(Long id) {
        log.debug("Request to get DatelleCompra : {}", id);
        return datelleCompraRepository.findById(id).map(datelleCompraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DatelleCompra : {}", id);
        datelleCompraRepository.deleteById(id);
    }
}
