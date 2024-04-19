package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.TratamientoRepository;
import com.veterinary.sistema.service.TratamientoService;
import com.veterinary.sistema.service.dto.TratamientoDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Tratamiento}.
 */
@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoResource {

    private final Logger log = LoggerFactory.getLogger(TratamientoResource.class);

    private static final String ENTITY_NAME = "tratamiento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TratamientoService tratamientoService;

    private final TratamientoRepository tratamientoRepository;

    public TratamientoResource(TratamientoService tratamientoService, TratamientoRepository tratamientoRepository) {
        this.tratamientoService = tratamientoService;
        this.tratamientoRepository = tratamientoRepository;
    }

    /**
     * {@code POST  /tratamientos} : Create a new tratamiento.
     *
     * @param tratamientoDTO the tratamientoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tratamientoDTO, or with status {@code 400 (Bad Request)} if the tratamiento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TratamientoDTO> createTratamiento(@Valid @RequestBody TratamientoDTO tratamientoDTO) throws URISyntaxException {
        log.debug("REST request to save Tratamiento : {}", tratamientoDTO);
        if (tratamientoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tratamiento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tratamientoDTO = tratamientoService.save(tratamientoDTO);
        return ResponseEntity.created(new URI("/api/tratamientos/" + tratamientoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tratamientoDTO.getId().toString()))
            .body(tratamientoDTO);
    }

    /**
     * {@code PUT  /tratamientos/:id} : Updates an existing tratamiento.
     *
     * @param id the id of the tratamientoDTO to save.
     * @param tratamientoDTO the tratamientoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tratamientoDTO,
     * or with status {@code 400 (Bad Request)} if the tratamientoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tratamientoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TratamientoDTO> updateTratamiento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TratamientoDTO tratamientoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Tratamiento : {}, {}", id, tratamientoDTO);
        if (tratamientoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tratamientoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tratamientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tratamientoDTO = tratamientoService.update(tratamientoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tratamientoDTO.getId().toString()))
            .body(tratamientoDTO);
    }

    /**
     * {@code PATCH  /tratamientos/:id} : Partial updates given fields of an existing tratamiento, field will ignore if it is null
     *
     * @param id the id of the tratamientoDTO to save.
     * @param tratamientoDTO the tratamientoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tratamientoDTO,
     * or with status {@code 400 (Bad Request)} if the tratamientoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tratamientoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tratamientoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TratamientoDTO> partialUpdateTratamiento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TratamientoDTO tratamientoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tratamiento partially : {}, {}", id, tratamientoDTO);
        if (tratamientoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tratamientoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tratamientoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TratamientoDTO> result = tratamientoService.partialUpdate(tratamientoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tratamientoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tratamientos} : get all the tratamientos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tratamientos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TratamientoDTO>> getAllTratamientos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Tratamientos");
        Page<TratamientoDTO> page = tratamientoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tratamientos/:id} : get the "id" tratamiento.
     *
     * @param id the id of the tratamientoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tratamientoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TratamientoDTO> getTratamiento(@PathVariable("id") Long id) {
        log.debug("REST request to get Tratamiento : {}", id);
        Optional<TratamientoDTO> tratamientoDTO = tratamientoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tratamientoDTO);
    }

    /**
     * {@code DELETE  /tratamientos/:id} : delete the "id" tratamiento.
     *
     * @param id the id of the tratamientoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTratamiento(@PathVariable("id") Long id) {
        log.debug("REST request to delete Tratamiento : {}", id);
        tratamientoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
