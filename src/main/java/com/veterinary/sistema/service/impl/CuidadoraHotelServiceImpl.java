package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.CuidadoraHotel;
import com.veterinary.sistema.repository.CuidadoraHotelRepository;
import com.veterinary.sistema.service.CuidadoraHotelService;
import com.veterinary.sistema.service.dto.CuidadoraHotelDTO;
import com.veterinary.sistema.service.mapper.CuidadoraHotelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.CuidadoraHotel}.
 */
@Service
@Transactional
public class CuidadoraHotelServiceImpl implements CuidadoraHotelService {

    private final Logger log = LoggerFactory.getLogger(CuidadoraHotelServiceImpl.class);

    private final CuidadoraHotelRepository cuidadoraHotelRepository;

    private final CuidadoraHotelMapper cuidadoraHotelMapper;

    public CuidadoraHotelServiceImpl(CuidadoraHotelRepository cuidadoraHotelRepository, CuidadoraHotelMapper cuidadoraHotelMapper) {
        this.cuidadoraHotelRepository = cuidadoraHotelRepository;
        this.cuidadoraHotelMapper = cuidadoraHotelMapper;
    }

    @Override
    public CuidadoraHotelDTO save(CuidadoraHotelDTO cuidadoraHotelDTO) {
        log.debug("Request to save CuidadoraHotel : {}", cuidadoraHotelDTO);
        CuidadoraHotel cuidadoraHotel = cuidadoraHotelMapper.toEntity(cuidadoraHotelDTO);
        cuidadoraHotel = cuidadoraHotelRepository.save(cuidadoraHotel);
        return cuidadoraHotelMapper.toDto(cuidadoraHotel);
    }

    @Override
    public CuidadoraHotelDTO update(CuidadoraHotelDTO cuidadoraHotelDTO) {
        log.debug("Request to update CuidadoraHotel : {}", cuidadoraHotelDTO);
        CuidadoraHotel cuidadoraHotel = cuidadoraHotelMapper.toEntity(cuidadoraHotelDTO);
        cuidadoraHotel = cuidadoraHotelRepository.save(cuidadoraHotel);
        return cuidadoraHotelMapper.toDto(cuidadoraHotel);
    }

    @Override
    public Optional<CuidadoraHotelDTO> partialUpdate(CuidadoraHotelDTO cuidadoraHotelDTO) {
        log.debug("Request to partially update CuidadoraHotel : {}", cuidadoraHotelDTO);

        return cuidadoraHotelRepository
            .findById(cuidadoraHotelDTO.getId())
            .map(existingCuidadoraHotel -> {
                cuidadoraHotelMapper.partialUpdate(existingCuidadoraHotel, cuidadoraHotelDTO);

                return existingCuidadoraHotel;
            })
            .map(cuidadoraHotelRepository::save)
            .map(cuidadoraHotelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuidadoraHotelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CuidadoraHotels");
        return cuidadoraHotelRepository.findAll(pageable).map(cuidadoraHotelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuidadoraHotelDTO> findOne(Long id) {
        log.debug("Request to get CuidadoraHotel : {}", id);
        return cuidadoraHotelRepository.findById(id).map(cuidadoraHotelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CuidadoraHotel : {}", id);
        cuidadoraHotelRepository.deleteById(id);
    }
}
