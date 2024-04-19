package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.EspecieRepository;
import com.veterinary.sistema.service.EspecieService;
import com.veterinary.sistema.service.dto.EspecieDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.Especie}.
 */
@RestController
@RequestMapping("/api/especies")
public class EspecieResource {

    private final Logger log = LoggerFactory.getLogger(EspecieResource.class);

    private static final String ENTITY_NAME = "especie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EspecieService especieService;

    private final EspecieRepository especieRepository;

    public EspecieResource(EspecieService especieService, EspecieRepository especieRepository) {
        this.especieService = especieService;
        this.especieRepository = especieRepository;
    }

    /**
     * {@code POST  /especies} : Create a new especie.
     *
     * @param especieDTO the especieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new especieDTO, or with status {@code 400 (Bad Request)} if the especie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EspecieDTO> createEspecie(@Valid @RequestBody EspecieDTO especieDTO) throws URISyntaxException {
        log.debug("REST request to save Especie : {}", especieDTO);
        if (especieDTO.getId() != null) {
            throw new BadRequestAlertException("A new especie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        especieDTO = especieService.save(especieDTO);
        return ResponseEntity.created(new URI("/api/especies/" + especieDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, especieDTO.getId().toString()))
            .body(especieDTO);
    }

    /**
     * {@code PUT  /especies/:id} : Updates an existing especie.
     *
     * @param id the id of the especieDTO to save.
     * @param especieDTO the especieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especieDTO,
     * or with status {@code 400 (Bad Request)} if the especieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the especieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EspecieDTO> updateEspecie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EspecieDTO especieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Especie : {}, {}", id, especieDTO);
        if (especieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        especieDTO = especieService.update(especieDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, especieDTO.getId().toString()))
            .body(especieDTO);
    }

    /**
     * {@code PATCH  /especies/:id} : Partial updates given fields of an existing especie, field will ignore if it is null
     *
     * @param id the id of the especieDTO to save.
     * @param especieDTO the especieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especieDTO,
     * or with status {@code 400 (Bad Request)} if the especieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the especieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the especieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EspecieDTO> partialUpdateEspecie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EspecieDTO especieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Especie partially : {}, {}", id, especieDTO);
        if (especieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, especieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EspecieDTO> result = especieService.partialUpdate(especieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, especieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /especies} : get all the especies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of especies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EspecieDTO>> getAllEspecies(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Especies");
        Page<EspecieDTO> page = especieService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /especies/:id} : get the "id" especie.
     *
     * @param id the id of the especieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the especieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EspecieDTO> getEspecie(@PathVariable("id") Long id) {
        log.debug("REST request to get Especie : {}", id);
        Optional<EspecieDTO> especieDTO = especieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(especieDTO);
    }

    /**
     * {@code DELETE  /especies/:id} : delete the "id" especie.
     *
     * @param id the id of the especieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEspecie(@PathVariable("id") Long id) {
        log.debug("REST request to delete Especie : {}", id);
        especieService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
