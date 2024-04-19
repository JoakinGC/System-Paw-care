package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.VeterinarioRepository;
import com.veterinary.sistema.service.VeterinarioService;
import com.veterinary.sistema.service.dto.VeterinarioDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.veterinary.sistema.domain.Veterinario}.
 */
@RestController
@RequestMapping("/api/veterinarios")
public class VeterinarioResource {

    private final Logger log = LoggerFactory.getLogger(VeterinarioResource.class);

    private static final String ENTITY_NAME = "veterinario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VeterinarioService veterinarioService;

    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioResource(VeterinarioService veterinarioService, VeterinarioRepository veterinarioRepository) {
        this.veterinarioService = veterinarioService;
        this.veterinarioRepository = veterinarioRepository;
    }

    /**
     * {@code POST  /veterinarios} : Create a new veterinario.
     *
     * @param veterinarioDTO the veterinarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new veterinarioDTO, or with status {@code 400 (Bad Request)} if the veterinario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VeterinarioDTO> createVeterinario(@Valid @RequestBody VeterinarioDTO veterinarioDTO) throws URISyntaxException {
        log.debug("REST request to save Veterinario : {}", veterinarioDTO);
        if (veterinarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new veterinario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        veterinarioDTO = veterinarioService.save(veterinarioDTO);
        return ResponseEntity.created(new URI("/api/veterinarios/" + veterinarioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, veterinarioDTO.getId().toString()))
            .body(veterinarioDTO);
    }

    /**
     * {@code PUT  /veterinarios/:id} : Updates an existing veterinario.
     *
     * @param id the id of the veterinarioDTO to save.
     * @param veterinarioDTO the veterinarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated veterinarioDTO,
     * or with status {@code 400 (Bad Request)} if the veterinarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the veterinarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VeterinarioDTO> updateVeterinario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VeterinarioDTO veterinarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Veterinario : {}, {}", id, veterinarioDTO);
        if (veterinarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, veterinarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!veterinarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        veterinarioDTO = veterinarioService.update(veterinarioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, veterinarioDTO.getId().toString()))
            .body(veterinarioDTO);
    }

    /**
     * {@code PATCH  /veterinarios/:id} : Partial updates given fields of an existing veterinario, field will ignore if it is null
     *
     * @param id the id of the veterinarioDTO to save.
     * @param veterinarioDTO the veterinarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated veterinarioDTO,
     * or with status {@code 400 (Bad Request)} if the veterinarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the veterinarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the veterinarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VeterinarioDTO> partialUpdateVeterinario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VeterinarioDTO veterinarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Veterinario partially : {}, {}", id, veterinarioDTO);
        if (veterinarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, veterinarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!veterinarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VeterinarioDTO> result = veterinarioService.partialUpdate(veterinarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, veterinarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /veterinarios} : get all the veterinarios.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of veterinarios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VeterinarioDTO>> getAllVeterinarios(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("usuario-is-null".equals(filter)) {
            log.debug("REST request to get all Veterinarios where usuario is null");
            return new ResponseEntity<>(veterinarioService.findAllWhereUsuarioIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Veterinarios");
        Page<VeterinarioDTO> page = veterinarioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /veterinarios/:id} : get the "id" veterinario.
     *
     * @param id the id of the veterinarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the veterinarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VeterinarioDTO> getVeterinario(@PathVariable("id") Long id) {
        log.debug("REST request to get Veterinario : {}", id);
        Optional<VeterinarioDTO> veterinarioDTO = veterinarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(veterinarioDTO);
    }

    /**
     * {@code DELETE  /veterinarios/:id} : delete the "id" veterinario.
     *
     * @param id the id of the veterinarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinario(@PathVariable("id") Long id) {
        log.debug("REST request to delete Veterinario : {}", id);
        veterinarioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
