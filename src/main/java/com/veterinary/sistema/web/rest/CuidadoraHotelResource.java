package com.veterinary.sistema.web.rest;

import com.veterinary.sistema.repository.CuidadoraHotelRepository;
import com.veterinary.sistema.service.CuidadoraHotelService;
import com.veterinary.sistema.service.dto.CuidadoraHotelDTO;
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
 * REST controller for managing {@link com.veterinary.sistema.domain.CuidadoraHotel}.
 */
@RestController
@RequestMapping("/api/cuidadora-hotels")
public class CuidadoraHotelResource {

    private final Logger log = LoggerFactory.getLogger(CuidadoraHotelResource.class);

    private static final String ENTITY_NAME = "cuidadoraHotel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CuidadoraHotelService cuidadoraHotelService;

    private final CuidadoraHotelRepository cuidadoraHotelRepository;

    public CuidadoraHotelResource(CuidadoraHotelService cuidadoraHotelService, CuidadoraHotelRepository cuidadoraHotelRepository) {
        this.cuidadoraHotelService = cuidadoraHotelService;
        this.cuidadoraHotelRepository = cuidadoraHotelRepository;
    }

    /**
     * {@code POST  /cuidadora-hotels} : Create a new cuidadoraHotel.
     *
     * @param cuidadoraHotelDTO the cuidadoraHotelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cuidadoraHotelDTO, or with status {@code 400 (Bad Request)} if the cuidadoraHotel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CuidadoraHotelDTO> createCuidadoraHotel(@Valid @RequestBody CuidadoraHotelDTO cuidadoraHotelDTO)
        throws URISyntaxException {
        log.debug("REST request to save CuidadoraHotel : {}", cuidadoraHotelDTO);
        if (cuidadoraHotelDTO.getId() != null) {
            throw new BadRequestAlertException("A new cuidadoraHotel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cuidadoraHotelDTO = cuidadoraHotelService.save(cuidadoraHotelDTO);
        return ResponseEntity.created(new URI("/api/cuidadora-hotels/" + cuidadoraHotelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cuidadoraHotelDTO.getId().toString()))
            .body(cuidadoraHotelDTO);
    }

    /**
     * {@code PUT  /cuidadora-hotels/:id} : Updates an existing cuidadoraHotel.
     *
     * @param id the id of the cuidadoraHotelDTO to save.
     * @param cuidadoraHotelDTO the cuidadoraHotelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuidadoraHotelDTO,
     * or with status {@code 400 (Bad Request)} if the cuidadoraHotelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuidadoraHotelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CuidadoraHotelDTO> updateCuidadoraHotel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CuidadoraHotelDTO cuidadoraHotelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CuidadoraHotel : {}, {}", id, cuidadoraHotelDTO);
        if (cuidadoraHotelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuidadoraHotelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuidadoraHotelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cuidadoraHotelDTO = cuidadoraHotelService.update(cuidadoraHotelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuidadoraHotelDTO.getId().toString()))
            .body(cuidadoraHotelDTO);
    }

    /**
     * {@code PATCH  /cuidadora-hotels/:id} : Partial updates given fields of an existing cuidadoraHotel, field will ignore if it is null
     *
     * @param id the id of the cuidadoraHotelDTO to save.
     * @param cuidadoraHotelDTO the cuidadoraHotelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuidadoraHotelDTO,
     * or with status {@code 400 (Bad Request)} if the cuidadoraHotelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cuidadoraHotelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cuidadoraHotelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CuidadoraHotelDTO> partialUpdateCuidadoraHotel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CuidadoraHotelDTO cuidadoraHotelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CuidadoraHotel partially : {}, {}", id, cuidadoraHotelDTO);
        if (cuidadoraHotelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuidadoraHotelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuidadoraHotelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CuidadoraHotelDTO> result = cuidadoraHotelService.partialUpdate(cuidadoraHotelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuidadoraHotelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cuidadora-hotels} : get all the cuidadoraHotels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cuidadoraHotels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CuidadoraHotelDTO>> getAllCuidadoraHotels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CuidadoraHotels");
        Page<CuidadoraHotelDTO> page = cuidadoraHotelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cuidadora-hotels/:id} : get the "id" cuidadoraHotel.
     *
     * @param id the id of the cuidadoraHotelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuidadoraHotelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CuidadoraHotelDTO> getCuidadoraHotel(@PathVariable("id") Long id) {
        log.debug("REST request to get CuidadoraHotel : {}", id);
        Optional<CuidadoraHotelDTO> cuidadoraHotelDTO = cuidadoraHotelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cuidadoraHotelDTO);
    }

    /**
     * {@code DELETE  /cuidadora-hotels/:id} : delete the "id" cuidadoraHotel.
     *
     * @param id the id of the cuidadoraHotelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuidadoraHotel(@PathVariable("id") Long id) {
        log.debug("REST request to delete CuidadoraHotel : {}", id);
        cuidadoraHotelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
