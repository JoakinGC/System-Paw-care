package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.EsteticaRepository;
import com.veterinary.sistema.service.EsteticaService;
import com.veterinary.sistema.service.dto.EsteticaDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Estetica}.
 */
@RestController
@RequestMapping("/api/esteticas")
public class EsteticaResource {

    private final Logger log = LoggerFactory.getLogger(EsteticaResource.class);

    private static final String ENTITY_NAME = "estetica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EsteticaService esteticaService;

    private final EsteticaRepository esteticaRepository;

    public EsteticaResource(EsteticaService esteticaService, EsteticaRepository esteticaRepository) {
        this.esteticaService = esteticaService;
        this.esteticaRepository = esteticaRepository;
    }

    /**
     * {@code POST  /esteticas} : Create a new estetica.
     *
     * @param esteticaDTO the esteticaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new esteticaDTO, or with status {@code 400 (Bad Request)} if the estetica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EsteticaDTO> createEstetica(@Valid @RequestBody EsteticaDTO esteticaDTO) throws URISyntaxException {
        log.debug("REST request to save Estetica : {}", esteticaDTO);
        if (esteticaDTO.getId() != null) {
            throw new BadRequestAlertException("A new estetica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        esteticaDTO = esteticaService.save(esteticaDTO);
        return ResponseEntity.created(new URI("/api/esteticas/" + esteticaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, esteticaDTO.getId().toString()))
            .body(esteticaDTO);
    }

    /**
     * {@code PUT  /esteticas/:id} : Updates an existing estetica.
     *
     * @param id the id of the esteticaDTO to save.
     * @param esteticaDTO the esteticaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esteticaDTO,
     * or with status {@code 400 (Bad Request)} if the esteticaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the esteticaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EsteticaDTO> updateEstetica(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EsteticaDTO esteticaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Estetica : {}, {}", id, esteticaDTO);
        if (esteticaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, esteticaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!esteticaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        esteticaDTO = esteticaService.update(esteticaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, esteticaDTO.getId().toString()))
            .body(esteticaDTO);
    }

    /**
     * {@code PATCH  /esteticas/:id} : Partial updates given fields of an existing estetica, field will ignore if it is null
     *
     * @param id the id of the esteticaDTO to save.
     * @param esteticaDTO the esteticaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esteticaDTO,
     * or with status {@code 400 (Bad Request)} if the esteticaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the esteticaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the esteticaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EsteticaDTO> partialUpdateEstetica(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EsteticaDTO esteticaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Estetica partially : {}, {}", id, esteticaDTO);
        if (esteticaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, esteticaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!esteticaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EsteticaDTO> result = esteticaService.partialUpdate(esteticaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, esteticaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /esteticas} : get all the esteticas.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of esteticas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EsteticaDTO>> getAllEsteticas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("usuario-is-null".equals(filter)) {
            log.debug("REST request to get all Esteticas where usuario is null");
            return new ResponseEntity<>(esteticaService.findAllWhereUsuarioIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Esteticas");
        Page<EsteticaDTO> page = esteticaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /esteticas/:id} : get the "id" estetica.
     *
     * @param id the id of the esteticaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the esteticaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EsteticaDTO> getEstetica(@PathVariable("id") Long id) {
        log.debug("REST request to get Estetica : {}", id);
        Optional<EsteticaDTO> esteticaDTO = esteticaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(esteticaDTO);
    }

    /**
     * {@code DELETE  /esteticas/:id} : delete the "id" estetica.
     *
     * @param id the id of the esteticaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstetica(@PathVariable("id") Long id) {
        log.debug("REST request to delete Estetica : {}", id);
        esteticaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
