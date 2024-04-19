package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.EstudiosRepository;
import com.veterinary.sistema.service.EstudiosService;
import com.veterinary.sistema.service.dto.EstudiosDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Estudios}.
 */
@RestController
@RequestMapping("/api/estudios")
public class EstudiosResource {

    private final Logger log = LoggerFactory.getLogger(EstudiosResource.class);

    private static final String ENTITY_NAME = "estudios";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstudiosService estudiosService;

    private final EstudiosRepository estudiosRepository;

    public EstudiosResource(EstudiosService estudiosService, EstudiosRepository estudiosRepository) {
        this.estudiosService = estudiosService;
        this.estudiosRepository = estudiosRepository;
    }

    /**
     * {@code POST  /estudios} : Create a new estudios.
     *
     * @param estudiosDTO the estudiosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estudiosDTO, or with status {@code 400 (Bad Request)} if the estudios has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EstudiosDTO> createEstudios(@Valid @RequestBody EstudiosDTO estudiosDTO) throws URISyntaxException {
        log.debug("REST request to save Estudios : {}", estudiosDTO);
        if (estudiosDTO.getId() != null) {
            throw new BadRequestAlertException("A new estudios cannot already have an ID", ENTITY_NAME, "idexists");
        }
        estudiosDTO = estudiosService.save(estudiosDTO);
        return ResponseEntity.created(new URI("/api/estudios/" + estudiosDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, estudiosDTO.getId().toString()))
            .body(estudiosDTO);
    }

    /**
     * {@code PUT  /estudios/:id} : Updates an existing estudios.
     *
     * @param id the id of the estudiosDTO to save.
     * @param estudiosDTO the estudiosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estudiosDTO,
     * or with status {@code 400 (Bad Request)} if the estudiosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estudiosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EstudiosDTO> updateEstudios(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EstudiosDTO estudiosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Estudios : {}, {}", id, estudiosDTO);
        if (estudiosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estudiosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estudiosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        estudiosDTO = estudiosService.update(estudiosDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, estudiosDTO.getId().toString()))
            .body(estudiosDTO);
    }

    /**
     * {@code PATCH  /estudios/:id} : Partial updates given fields of an existing estudios, field will ignore if it is null
     *
     * @param id the id of the estudiosDTO to save.
     * @param estudiosDTO the estudiosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estudiosDTO,
     * or with status {@code 400 (Bad Request)} if the estudiosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the estudiosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the estudiosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EstudiosDTO> partialUpdateEstudios(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EstudiosDTO estudiosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Estudios partially : {}, {}", id, estudiosDTO);
        if (estudiosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, estudiosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!estudiosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EstudiosDTO> result = estudiosService.partialUpdate(estudiosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, estudiosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /estudios} : get all the estudios.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estudios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EstudiosDTO>> getAllEstudios(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Estudios");
        Page<EstudiosDTO> page;
        if (eagerload) {
            page = estudiosService.findAllWithEagerRelationships(pageable);
        } else {
            page = estudiosService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /estudios/:id} : get the "id" estudios.
     *
     * @param id the id of the estudiosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estudiosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EstudiosDTO> getEstudios(@PathVariable("id") Long id) {
        log.debug("REST request to get Estudios : {}", id);
        Optional<EstudiosDTO> estudiosDTO = estudiosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estudiosDTO);
    }

    /**
     * {@code DELETE  /estudios/:id} : delete the "id" estudios.
     *
     * @param id the id of the estudiosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstudios(@PathVariable("id") Long id) {
        log.debug("REST request to delete Estudios : {}", id);
        estudiosService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
