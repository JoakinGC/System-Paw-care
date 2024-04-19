package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.MedicamentoRepository;
import com.veterinary.sistema.service.MedicamentoService;
import com.veterinary.sistema.service.dto.MedicamentoDTO;
import com.veterinary.sistema.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.veterinary.sistema.domain.Medicamento}.
 */
@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoResource {

    private final Logger log = LoggerFactory.getLogger(MedicamentoResource.class);

    private static final String ENTITY_NAME = "medicamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicamentoService medicamentoService;

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoResource(MedicamentoService medicamentoService, MedicamentoRepository medicamentoRepository) {
        this.medicamentoService = medicamentoService;
        this.medicamentoRepository = medicamentoRepository;
    }

    /**
     * {@code POST  /medicamentos} : Create a new medicamento.
     *
     * @param medicamentoDTO the medicamentoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicamentoDTO, or with status {@code 400 (Bad Request)} if the medicamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedicamentoDTO> createMedicamento(@Valid @RequestBody MedicamentoDTO medicamentoDTO) throws URISyntaxException {
        log.debug("REST request to save Medicamento : {}", medicamentoDTO);
        if (medicamentoDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medicamentoDTO = medicamentoService.save(medicamentoDTO);
        return ResponseEntity.created(new URI("/api/medicamentos/" + medicamentoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, medicamentoDTO.getId().toString()))
            .body(medicamentoDTO);
    }

    /**
     * {@code PUT  /medicamentos/:id} : Updates an existing medicamento.
     *
     * @param id the id of the medicamentoDTO to save.
     * @param medicamentoDTO the medicamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentoDTO,
     * or with status {@code 400 (Bad Request)} if the medicamentoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> updateMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicamentoDTO medicamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Medicamento : {}, {}", id, medicamentoDTO);
        if (medicamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medicamentoDTO = medicamentoService.update(medicamentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamentoDTO.getId().toString()))
            .body(medicamentoDTO);
    }

    /**
     * {@code PATCH  /medicamentos/:id} : Partial updates given fields of an existing medicamento, field will ignore if it is null
     *
     * @param id the id of the medicamentoDTO to save.
     * @param medicamentoDTO the medicamentoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamentoDTO,
     * or with status {@code 400 (Bad Request)} if the medicamentoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicamentoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicamentoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicamentoDTO> partialUpdateMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicamentoDTO medicamentoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medicamento partially : {}, {}", id, medicamentoDTO);
        if (medicamentoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamentoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicamentoDTO> result = medicamentoService.partialUpdate(medicamentoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamentoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medicamentos} : get all the medicamentos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicamentos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MedicamentoDTO>> getAllMedicamentos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Medicamentos");
        Page<MedicamentoDTO> page = medicamentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medicamentos/:id} : get the "id" medicamento.
     *
     * @param id the id of the medicamentoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicamentoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoDTO> getMedicamento(@PathVariable("id") Long id) {
        log.debug("REST request to get Medicamento : {}", id);
        Optional<MedicamentoDTO> medicamentoDTO = medicamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicamentoDTO);
    }

    /**
     * {@code DELETE  /medicamentos/:id} : delete the "id" medicamento.
     *
     * @param id the id of the medicamentoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicamento(@PathVariable("id") Long id) {
        log.debug("REST request to delete Medicamento : {}", id);
        medicamentoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
