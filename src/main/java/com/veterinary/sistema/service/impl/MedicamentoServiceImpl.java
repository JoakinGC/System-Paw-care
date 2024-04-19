package com.veterinary.sistema.service.impl;

import com.veterinary.sistema.domain.Medicamento;
import com.veterinary.sistema.repository.MedicamentoRepository;
import com.veterinary.sistema.service.MedicamentoService;
import com.veterinary.sistema.service.dto.MedicamentoDTO;
import com.veterinary.sistema.service.mapper.MedicamentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.veterinary.sistema.domain.Medicamento}.
 */
@Service
@Transactional
public class MedicamentoServiceImpl implements MedicamentoService {

    private final Logger log = LoggerFactory.getLogger(MedicamentoServiceImpl.class);

    private final MedicamentoRepository medicamentoRepository;

    private final MedicamentoMapper medicamentoMapper;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository, MedicamentoMapper medicamentoMapper) {
        this.medicamentoRepository = medicamentoRepository;
        this.medicamentoMapper = medicamentoMapper;
    }

    @Override
    public MedicamentoDTO save(MedicamentoDTO medicamentoDTO) {
        log.debug("Request to save Medicamento : {}", medicamentoDTO);
        Medicamento medicamento = medicamentoMapper.toEntity(medicamentoDTO);
        medicamento = medicamentoRepository.save(medicamento);
        return medicamentoMapper.toDto(medicamento);
    }

    @Override
    public MedicamentoDTO update(MedicamentoDTO medicamentoDTO) {
        log.debug("Request to update Medicamento : {}", medicamentoDTO);
        Medicamento medicamento = medicamentoMapper.toEntity(medicamentoDTO);
        medicamento = medicamentoRepository.save(medicamento);
        return medicamentoMapper.toDto(medicamento);
    }

    @Override
    public Optional<MedicamentoDTO> partialUpdate(MedicamentoDTO medicamentoDTO) {
        log.debug("Request to partially update Medicamento : {}", medicamentoDTO);

        return medicamentoRepository
            .findById(medicamentoDTO.getId())
            .map(existingMedicamento -> {
                medicamentoMapper.partialUpdate(existingMedicamento, medicamentoDTO);

                return existingMedicamento;
            })
            .map(medicamentoRepository::save)
            .map(medicamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medicamentos");
        return medicamentoRepository.findAll(pageable).map(medicamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicamentoDTO> findOne(Long id) {
        log.debug("Request to get Medicamento : {}", id);
        return medicamentoRepository.findById(id).map(medicamentoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Medicamento : {}", id);
        medicamentoRepository.deleteById(id);
    }
}
