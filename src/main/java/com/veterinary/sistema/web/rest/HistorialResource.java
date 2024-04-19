package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.HistorialRepository;
import com.veterinary.sistema.service.HistorialService;
import com.veterinary.sistema.service.dto.HistorialDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Historial}.
 */
@RestController
@RequestMapping("/api/historials")
public class HistorialResource {

    private final Logger log = LoggerFactory.getLogger(HistorialResource.class);

    private static final String ENTITY_NAME = "historial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistorialService historialService;

    private final HistorialRepository historialRepository;

    public HistorialResource(HistorialService historialService, HistorialRepository historialRepository) {
        this.historialService = historialService;
        this.historialRepository = historialRepository;
    }

    /**
     * {@code POST  /historials} : Create a new historial.
     *
     * @param historialDTO the historialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historialDTO, or with status {@code 400 (Bad Request)} if the historial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HistorialDTO> createHistorial(@Valid @RequestBody HistorialDTO historialDTO) throws URISyntaxException {
        log.debug("REST request to save Historial : {}", historialDTO);
        if (historialDTO.getId() != null) {
            throw new BadRequestAlertException("A new historial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        historialDTO = historialService.save(historialDTO);
        return ResponseEntity.created(new URI("/api/historials/" + historialDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, historialDTO.getId().toString()))
            .body(historialDTO);
    }

    /**
     * {@code PUT  /historials/:id} : Updates an existing historial.
     *
     * @param id the id of the historialDTO to save.
     * @param historialDTO the historialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historialDTO,
     * or with status {@code 400 (Bad Request)} if the historialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistorialDTO> updateHistorial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistorialDTO historialDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Historial : {}, {}", id, historialDTO);
        if (historialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        historialDTO = historialService.update(historialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historialDTO.getId().toString()))
            .body(historialDTO);
    }

    /**
     * {@code PATCH  /historials/:id} : Partial updates given fields of an existing historial, field will ignore if it is null
     *
     * @param id the id of the historialDTO to save.
     * @param historialDTO the historialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historialDTO,
     * or with status {@code 400 (Bad Request)} if the historialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistorialDTO> partialUpdateHistorial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistorialDTO historialDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Historial partially : {}, {}", id, historialDTO);
        if (historialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistorialDTO> result = historialService.partialUpdate(historialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historials} : get all the historials.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historials in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistorialDTO>> getAllHistorials(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Historials");
        Page<HistorialDTO> page;
        if (eagerload) {
            page = historialService.findAllWithEagerRelationships(pageable);
        } else {
            page = historialService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historials/:id} : get the "id" historial.
     *
     * @param id the id of the historialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistorialDTO> getHistorial(@PathVariable("id") Long id) {
        log.debug("REST request to get Historial : {}", id);
        Optional<HistorialDTO> historialDTO = historialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historialDTO);
    }

    /**
     * {@code DELETE  /historials/:id} : delete the "id" historial.
     *
     * @param id the id of the historialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorial(@PathVariable("id") Long id) {
        log.debug("REST request to delete Historial : {}", id);
        historialService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
