package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.EnfermedadRepository;
import com.veterinary.sistema.service.EnfermedadService;
import com.veterinary.sistema.service.dto.EnfermedadDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Enfermedad}.
 */
@RestController
@RequestMapping("/api/enfermedads")
public class EnfermedadResource {

    private final Logger log = LoggerFactory.getLogger(EnfermedadResource.class);

    private static final String ENTITY_NAME = "enfermedad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnfermedadService enfermedadService;

    private final EnfermedadRepository enfermedadRepository;

    public EnfermedadResource(EnfermedadService enfermedadService, EnfermedadRepository enfermedadRepository) {
        this.enfermedadService = enfermedadService;
        this.enfermedadRepository = enfermedadRepository;
    }

    /**
     * {@code POST  /enfermedads} : Create a new enfermedad.
     *
     * @param enfermedadDTO the enfermedadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enfermedadDTO, or with status {@code 400 (Bad Request)} if the enfermedad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EnfermedadDTO> createEnfermedad(@Valid @RequestBody EnfermedadDTO enfermedadDTO) throws URISyntaxException {
        log.debug("REST request to save Enfermedad : {}", enfermedadDTO);
        if (enfermedadDTO.getId() != null) {
            throw new BadRequestAlertException("A new enfermedad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        enfermedadDTO = enfermedadService.save(enfermedadDTO);
        return ResponseEntity.created(new URI("/api/enfermedads/" + enfermedadDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, enfermedadDTO.getId().toString()))
            .body(enfermedadDTO);
    }

    /**
     * {@code PUT  /enfermedads/:id} : Updates an existing enfermedad.
     *
     * @param id the id of the enfermedadDTO to save.
     * @param enfermedadDTO the enfermedadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermedadDTO,
     * or with status {@code 400 (Bad Request)} if the enfermedadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enfermedadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnfermedadDTO> updateEnfermedad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EnfermedadDTO enfermedadDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Enfermedad : {}, {}", id, enfermedadDTO);
        if (enfermedadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfermedadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enfermedadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        enfermedadDTO = enfermedadService.update(enfermedadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enfermedadDTO.getId().toString()))
            .body(enfermedadDTO);
    }

    /**
     * {@code PATCH  /enfermedads/:id} : Partial updates given fields of an existing enfermedad, field will ignore if it is null
     *
     * @param id the id of the enfermedadDTO to save.
     * @param enfermedadDTO the enfermedadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enfermedadDTO,
     * or with status {@code 400 (Bad Request)} if the enfermedadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enfermedadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enfermedadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EnfermedadDTO> partialUpdateEnfermedad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EnfermedadDTO enfermedadDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enfermedad partially : {}, {}", id, enfermedadDTO);
        if (enfermedadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enfermedadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enfermedadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EnfermedadDTO> result = enfermedadService.partialUpdate(enfermedadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enfermedadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /enfermedads} : get all the enfermedads.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enfermedads in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EnfermedadDTO>> getAllEnfermedads(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Enfermedads");
        Page<EnfermedadDTO> page;
        if (eagerload) {
            page = enfermedadService.findAllWithEagerRelationships(pageable);
        } else {
            page = enfermedadService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enfermedads/:id} : get the "id" enfermedad.
     *
     * @param id the id of the enfermedadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enfermedadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnfermedadDTO> getEnfermedad(@PathVariable("id") Long id) {
        log.debug("REST request to get Enfermedad : {}", id);
        Optional<EnfermedadDTO> enfermedadDTO = enfermedadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enfermedadDTO);
    }

    /**
     * {@code DELETE  /enfermedads/:id} : delete the "id" enfermedad.
     *
     * @param id the id of the enfermedadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnfermedad(@PathVariable("id") Long id) {
        log.debug("REST request to delete Enfermedad : {}", id);
        enfermedadService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
