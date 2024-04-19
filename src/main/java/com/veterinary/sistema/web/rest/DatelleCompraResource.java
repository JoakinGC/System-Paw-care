package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.DatelleCompraRepository;
import com.veterinary.sistema.service.DatelleCompraService;
import com.veterinary.sistema.service.dto.DatelleCompraDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.DatelleCompra}.
 */
@RestController
@RequestMapping("/api/datelle-compras")
public class DatelleCompraResource {

    private final Logger log = LoggerFactory.getLogger(DatelleCompraResource.class);

    private static final String ENTITY_NAME = "datelleCompra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DatelleCompraService datelleCompraService;

    private final DatelleCompraRepository datelleCompraRepository;

    public DatelleCompraResource(DatelleCompraService datelleCompraService, DatelleCompraRepository datelleCompraRepository) {
        this.datelleCompraService = datelleCompraService;
        this.datelleCompraRepository = datelleCompraRepository;
    }

    /**
     * {@code POST  /datelle-compras} : Create a new datelleCompra.
     *
     * @param datelleCompraDTO the datelleCompraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new datelleCompraDTO, or with status {@code 400 (Bad Request)} if the datelleCompra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DatelleCompraDTO> createDatelleCompra(@Valid @RequestBody DatelleCompraDTO datelleCompraDTO)
        throws URISyntaxException {
        log.debug("REST request to save DatelleCompra : {}", datelleCompraDTO);
        if (datelleCompraDTO.getId() != null) {
            throw new BadRequestAlertException("A new datelleCompra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        datelleCompraDTO = datelleCompraService.save(datelleCompraDTO);
        return ResponseEntity.created(new URI("/api/datelle-compras/" + datelleCompraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, datelleCompraDTO.getId().toString()))
            .body(datelleCompraDTO);
    }

    /**
     * {@code PUT  /datelle-compras/:id} : Updates an existing datelleCompra.
     *
     * @param id the id of the datelleCompraDTO to save.
     * @param datelleCompraDTO the datelleCompraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated datelleCompraDTO,
     * or with status {@code 400 (Bad Request)} if the datelleCompraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the datelleCompraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DatelleCompraDTO> updateDatelleCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DatelleCompraDTO datelleCompraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DatelleCompra : {}, {}", id, datelleCompraDTO);
        if (datelleCompraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, datelleCompraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!datelleCompraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        datelleCompraDTO = datelleCompraService.update(datelleCompraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, datelleCompraDTO.getId().toString()))
            .body(datelleCompraDTO);
    }

    /**
     * {@code PATCH  /datelle-compras/:id} : Partial updates given fields of an existing datelleCompra, field will ignore if it is null
     *
     * @param id the id of the datelleCompraDTO to save.
     * @param datelleCompraDTO the datelleCompraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated datelleCompraDTO,
     * or with status {@code 400 (Bad Request)} if the datelleCompraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the datelleCompraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the datelleCompraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DatelleCompraDTO> partialUpdateDatelleCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DatelleCompraDTO datelleCompraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DatelleCompra partially : {}, {}", id, datelleCompraDTO);
        if (datelleCompraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, datelleCompraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!datelleCompraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DatelleCompraDTO> result = datelleCompraService.partialUpdate(datelleCompraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, datelleCompraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /datelle-compras} : get all the datelleCompras.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of datelleCompras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DatelleCompraDTO>> getAllDatelleCompras(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DatelleCompras");
        Page<DatelleCompraDTO> page = datelleCompraService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /datelle-compras/:id} : get the "id" datelleCompra.
     *
     * @param id the id of the datelleCompraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the datelleCompraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DatelleCompraDTO> getDatelleCompra(@PathVariable("id") Long id) {
        log.debug("REST request to get DatelleCompra : {}", id);
        Optional<DatelleCompraDTO> datelleCompraDTO = datelleCompraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(datelleCompraDTO);
    }

    /**
     * {@code DELETE  /datelle-compras/:id} : delete the "id" datelleCompra.
     *
     * @param id the id of the datelleCompraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatelleCompra(@PathVariable("id") Long id) {
        log.debug("REST request to delete DatelleCompra : {}", id);
        datelleCompraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
