package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.RazaRepository;
import com.veterinary.sistema.service.RazaService;
import com.veterinary.sistema.service.dto.RazaDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Raza}.
 */
@RestController
@RequestMapping("/api/razas")
public class RazaResource {

    private final Logger log = LoggerFactory.getLogger(RazaResource.class);

    private static final String ENTITY_NAME = "raza";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RazaService razaService;

    private final RazaRepository razaRepository;

    public RazaResource(RazaService razaService, RazaRepository razaRepository) {
        this.razaService = razaService;
        this.razaRepository = razaRepository;
    }

    /**
     * {@code POST  /razas} : Create a new raza.
     *
     * @param razaDTO the razaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new razaDTO, or with status {@code 400 (Bad Request)} if the raza has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RazaDTO> createRaza(@Valid @RequestBody RazaDTO razaDTO) throws URISyntaxException {
        log.debug("REST request to save Raza : {}", razaDTO);
        if (razaDTO.getId() != null) {
            throw new BadRequestAlertException("A new raza cannot already have an ID", ENTITY_NAME, "idexists");
        }
        razaDTO = razaService.save(razaDTO);
        return ResponseEntity.created(new URI("/api/razas/" + razaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, razaDTO.getId().toString()))
            .body(razaDTO);
    }

    /**
     * {@code PUT  /razas/:id} : Updates an existing raza.
     *
     * @param id the id of the razaDTO to save.
     * @param razaDTO the razaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated razaDTO,
     * or with status {@code 400 (Bad Request)} if the razaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the razaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RazaDTO> updateRaza(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RazaDTO razaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Raza : {}, {}", id, razaDTO);
        if (razaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, razaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!razaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        razaDTO = razaService.update(razaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, razaDTO.getId().toString()))
            .body(razaDTO);
    }

    /**
     * {@code PATCH  /razas/:id} : Partial updates given fields of an existing raza, field will ignore if it is null
     *
     * @param id the id of the razaDTO to save.
     * @param razaDTO the razaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated razaDTO,
     * or with status {@code 400 (Bad Request)} if the razaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the razaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the razaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RazaDTO> partialUpdateRaza(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RazaDTO razaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Raza partially : {}, {}", id, razaDTO);
        if (razaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, razaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!razaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RazaDTO> result = razaService.partialUpdate(razaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, razaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /razas} : get all the razas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of razas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RazaDTO>> getAllRazas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Razas");
        Page<RazaDTO> page = razaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /razas/:id} : get the "id" raza.
     *
     * @param id the id of the razaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the razaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RazaDTO> getRaza(@PathVariable("id") Long id) {
        log.debug("REST request to get Raza : {}", id);
        Optional<RazaDTO> razaDTO = razaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(razaDTO);
    }

    /**
     * {@code DELETE  /razas/:id} : delete the "id" raza.
     *
     * @param id the id of the razaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRaza(@PathVariable("id") Long id) {
        log.debug("REST request to delete Raza : {}", id);
        razaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
