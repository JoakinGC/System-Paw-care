package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.MascotaRepository;
import com.veterinary.sistema.service.MascotaService;
import com.veterinary.sistema.service.dto.MascotaDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Mascota}.
 */
@RestController
@RequestMapping("/api/mascotas")
public class MascotaResource {

    private final Logger log = LoggerFactory.getLogger(MascotaResource.class);

    private static final String ENTITY_NAME = "mascota";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MascotaService mascotaService;

    private final MascotaRepository mascotaRepository;

    public MascotaResource(MascotaService mascotaService, MascotaRepository mascotaRepository) {
        this.mascotaService = mascotaService;
        this.mascotaRepository = mascotaRepository;
    }

    /**
     * {@code POST  /mascotas} : Create a new mascota.
     *
     * @param mascotaDTO the mascotaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mascotaDTO, or with status {@code 400 (Bad Request)} if the mascota has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MascotaDTO> createMascota(@Valid @RequestBody MascotaDTO mascotaDTO) throws URISyntaxException {
        log.debug("REST request to save Mascota : {}", mascotaDTO);
        if (mascotaDTO.getId() != null) {
            throw new BadRequestAlertException("A new mascota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mascotaDTO = mascotaService.save(mascotaDTO);
        return ResponseEntity.created(new URI("/api/mascotas/" + mascotaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, mascotaDTO.getId().toString()))
            .body(mascotaDTO);
    }

    /**
     * {@code PUT  /mascotas/:id} : Updates an existing mascota.
     *
     * @param id the id of the mascotaDTO to save.
     * @param mascotaDTO the mascotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mascotaDTO,
     * or with status {@code 400 (Bad Request)} if the mascotaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mascotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MascotaDTO> updateMascota(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MascotaDTO mascotaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Mascota : {}, {}", id, mascotaDTO);
        if (mascotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mascotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mascotaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        mascotaDTO = mascotaService.update(mascotaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mascotaDTO.getId().toString()))
            .body(mascotaDTO);
    }

    /**
     * {@code PATCH  /mascotas/:id} : Partial updates given fields of an existing mascota, field will ignore if it is null
     *
     * @param id the id of the mascotaDTO to save.
     * @param mascotaDTO the mascotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mascotaDTO,
     * or with status {@code 400 (Bad Request)} if the mascotaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mascotaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mascotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MascotaDTO> partialUpdateMascota(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MascotaDTO mascotaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mascota partially : {}, {}", id, mascotaDTO);
        if (mascotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mascotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mascotaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MascotaDTO> result = mascotaService.partialUpdate(mascotaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mascotaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mascotas} : get all the mascotas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mascotas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MascotaDTO>> getAllMascotas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Mascotas");
        Page<MascotaDTO> page;
        if (eagerload) {
            page = mascotaService.findAllWithEagerRelationships(pageable);
        } else {
            page = mascotaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mascotas/:id} : get the "id" mascota.
     *
     * @param id the id of the mascotaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mascotaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MascotaDTO> getMascota(@PathVariable("id") Long id) {
        log.debug("REST request to get Mascota : {}", id);
        Optional<MascotaDTO> mascotaDTO = mascotaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mascotaDTO);
    }

    /**
     * {@code DELETE  /mascotas/:id} : delete the "id" mascota.
     *
     * @param id the id of the mascotaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMascota(@PathVariable("id") Long id) {
        log.debug("REST request to delete Mascota : {}", id);
        mascotaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
