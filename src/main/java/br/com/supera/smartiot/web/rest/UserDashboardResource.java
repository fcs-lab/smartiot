package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.UserDashboardRepository;
import br.com.supera.smartiot.service.UserDashboardService;
import br.com.supera.smartiot.service.dto.UserDashboardDTO;
import br.com.supera.smartiot.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.UserDashboard}.
 */
@RestController
@RequestMapping("/api/user-dashboards")
public class UserDashboardResource {

    private final Logger log = LoggerFactory.getLogger(UserDashboardResource.class);

    private static final String ENTITY_NAME = "userDashboard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDashboardService userDashboardService;

    private final UserDashboardRepository userDashboardRepository;

    public UserDashboardResource(UserDashboardService userDashboardService, UserDashboardRepository userDashboardRepository) {
        this.userDashboardService = userDashboardService;
        this.userDashboardRepository = userDashboardRepository;
    }

    /**
     * {@code POST  /user-dashboards} : Create a new userDashboard.
     *
     * @param userDashboardDTO the userDashboardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDashboardDTO, or with status {@code 400 (Bad Request)} if the userDashboard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserDashboardDTO> createUserDashboard(@Valid @RequestBody UserDashboardDTO userDashboardDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserDashboard : {}", userDashboardDTO);
        if (userDashboardDTO.getId() != null) {
            throw new BadRequestAlertException("A new userDashboard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userDashboardDTO = userDashboardService.save(userDashboardDTO);
        return ResponseEntity.created(new URI("/api/user-dashboards/" + userDashboardDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userDashboardDTO.getId().toString()))
            .body(userDashboardDTO);
    }

    /**
     * {@code PUT  /user-dashboards/:id} : Updates an existing userDashboard.
     *
     * @param id the id of the userDashboardDTO to save.
     * @param userDashboardDTO the userDashboardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDashboardDTO,
     * or with status {@code 400 (Bad Request)} if the userDashboardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDashboardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDashboardDTO> updateUserDashboard(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserDashboardDTO userDashboardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserDashboard : {}, {}", id, userDashboardDTO);
        if (userDashboardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDashboardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDashboardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userDashboardDTO = userDashboardService.update(userDashboardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDashboardDTO.getId().toString()))
            .body(userDashboardDTO);
    }

    /**
     * {@code PATCH  /user-dashboards/:id} : Partial updates given fields of an existing userDashboard, field will ignore if it is null
     *
     * @param id the id of the userDashboardDTO to save.
     * @param userDashboardDTO the userDashboardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDashboardDTO,
     * or with status {@code 400 (Bad Request)} if the userDashboardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userDashboardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDashboardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserDashboardDTO> partialUpdateUserDashboard(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserDashboardDTO userDashboardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserDashboard partially : {}, {}", id, userDashboardDTO);
        if (userDashboardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDashboardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDashboardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDashboardDTO> result = userDashboardService.partialUpdate(userDashboardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDashboardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-dashboards} : get all the userDashboards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDashboards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserDashboardDTO>> getAllUserDashboards(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserDashboards");
        Page<UserDashboardDTO> page = userDashboardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-dashboards/:id} : get the "id" userDashboard.
     *
     * @param id the id of the userDashboardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDashboardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDashboardDTO> getUserDashboard(@PathVariable("id") Long id) {
        log.debug("REST request to get UserDashboard : {}", id);
        Optional<UserDashboardDTO> userDashboardDTO = userDashboardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDashboardDTO);
    }

    /**
     * {@code DELETE  /user-dashboards/:id} : delete the "id" userDashboard.
     *
     * @param id the id of the userDashboardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDashboard(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserDashboard : {}", id);
        userDashboardService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
