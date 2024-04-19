package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.FactoresRepository;
import com.veterinary.sistema.service.FactoresService;
import com.veterinary.sistema.service.dto.FactoresDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Factores}.
 */
@RestController
@RequestMapping("/api/factores")
public class FactoresResource {

    private final Logger log = LoggerFactory.getLogger(FactoresResource.class);

    private static final String ENTITY_NAME = "factores";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactoresService factoresService;

    private final FactoresRepository factoresRepository;

    public FactoresResource(FactoresService factoresService, FactoresRepository factoresRepository) {
        this.factoresService = factoresService;
        this.factoresRepository = factoresRepository;
    }

    /**
     * {@code POST  /factores} : Create a new factores.
     *
     * @param factoresDTO the factoresDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factoresDTO, or with status {@code 400 (Bad Request)} if the factores has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactoresDTO> createFactores(@Valid @RequestBody FactoresDTO factoresDTO) throws URISyntaxException {
        log.debug("REST request to save Factores : {}", factoresDTO);
        if (factoresDTO.getId() != null) {
            throw new BadRequestAlertException("A new factores cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factoresDTO = factoresService.save(factoresDTO);
        return ResponseEntity.created(new URI("/api/factores/" + factoresDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, factoresDTO.getId().toString()))
            .body(factoresDTO);
    }

    /**
     * {@code PUT  /factores/:id} : Updates an existing factores.
     *
     * @param id the id of the factoresDTO to save.
     * @param factoresDTO the factoresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factoresDTO,
     * or with status {@code 400 (Bad Request)} if the factoresDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factoresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactoresDTO> updateFactores(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FactoresDTO factoresDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Factores : {}, {}", id, factoresDTO);
        if (factoresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factoresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factoresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factoresDTO = factoresService.update(factoresDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factoresDTO.getId().toString()))
            .body(factoresDTO);
    }

    /**
     * {@code PATCH  /factores/:id} : Partial updates given fields of an existing factores, field will ignore if it is null
     *
     * @param id the id of the factoresDTO to save.
     * @param factoresDTO the factoresDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factoresDTO,
     * or with status {@code 400 (Bad Request)} if the factoresDTO is not valid,
     * or with status {@code 404 (Not Found)} if the factoresDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the factoresDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactoresDTO> partialUpdateFactores(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FactoresDTO factoresDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Factores partially : {}, {}", id, factoresDTO);
        if (factoresDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factoresDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factoresRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactoresDTO> result = factoresService.partialUpdate(factoresDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, factoresDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /factores} : get all the factores.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factores in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FactoresDTO>> getAllFactores(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Factores");
        Page<FactoresDTO> page = factoresService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /factores/:id} : get the "id" factores.
     *
     * @param id the id of the factoresDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factoresDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactoresDTO> getFactores(@PathVariable("id") Long id) {
        log.debug("REST request to get Factores : {}", id);
        Optional<FactoresDTO> factoresDTO = factoresService.findOne(id);
        return ResponseUtil.wrapOrNotFound(factoresDTO);
    }

    /**
     * {@code DELETE  /factores/:id} : delete the "id" factores.
     *
     * @param id the id of the factoresDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactores(@PathVariable("id") Long id) {
        log.debug("REST request to delete Factores : {}", id);
        factoresService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
