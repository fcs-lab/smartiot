package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.ChatBookingRepository;
import br.com.supera.smartiot.service.ChatBookingService;
import br.com.supera.smartiot.service.dto.ChatBookingDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.ChatBooking}.
 */
@RestController
@RequestMapping("/api/chat-bookings")
public class ChatBookingResource {

    private final Logger log = LoggerFactory.getLogger(ChatBookingResource.class);

    private static final String ENTITY_NAME = "chatBooking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatBookingService chatBookingService;

    private final ChatBookingRepository chatBookingRepository;

    public ChatBookingResource(ChatBookingService chatBookingService, ChatBookingRepository chatBookingRepository) {
        this.chatBookingService = chatBookingService;
        this.chatBookingRepository = chatBookingRepository;
    }

    /**
     * {@code POST  /chat-bookings} : Create a new chatBooking.
     *
     * @param chatBookingDTO the chatBookingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatBookingDTO, or with status {@code 400 (Bad Request)} if the chatBooking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChatBookingDTO> createChatBooking(@Valid @RequestBody ChatBookingDTO chatBookingDTO) throws URISyntaxException {
        log.debug("REST request to save ChatBooking : {}", chatBookingDTO);
        if (chatBookingDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatBooking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chatBookingDTO = chatBookingService.save(chatBookingDTO);
        return ResponseEntity.created(new URI("/api/chat-bookings/" + chatBookingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chatBookingDTO.getId().toString()))
            .body(chatBookingDTO);
    }

    /**
     * {@code PUT  /chat-bookings/:id} : Updates an existing chatBooking.
     *
     * @param id the id of the chatBookingDTO to save.
     * @param chatBookingDTO the chatBookingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatBookingDTO,
     * or with status {@code 400 (Bad Request)} if the chatBookingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatBookingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChatBookingDTO> updateChatBooking(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChatBookingDTO chatBookingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChatBooking : {}, {}", id, chatBookingDTO);
        if (chatBookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatBookingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatBookingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chatBookingDTO = chatBookingService.update(chatBookingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatBookingDTO.getId().toString()))
            .body(chatBookingDTO);
    }

    /**
     * {@code PATCH  /chat-bookings/:id} : Partial updates given fields of an existing chatBooking, field will ignore if it is null
     *
     * @param id the id of the chatBookingDTO to save.
     * @param chatBookingDTO the chatBookingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatBookingDTO,
     * or with status {@code 400 (Bad Request)} if the chatBookingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatBookingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatBookingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChatBookingDTO> partialUpdateChatBooking(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChatBookingDTO chatBookingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChatBooking partially : {}, {}", id, chatBookingDTO);
        if (chatBookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatBookingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatBookingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChatBookingDTO> result = chatBookingService.partialUpdate(chatBookingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatBookingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chat-bookings} : get all the chatBookings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatBookings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChatBookingDTO>> getAllChatBookings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ChatBookings");
        Page<ChatBookingDTO> page = chatBookingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chat-bookings/:id} : get the "id" chatBooking.
     *
     * @param id the id of the chatBookingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatBookingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatBookingDTO> getChatBooking(@PathVariable("id") Long id) {
        log.debug("REST request to get ChatBooking : {}", id);
        Optional<ChatBookingDTO> chatBookingDTO = chatBookingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatBookingDTO);
    }

    /**
     * {@code DELETE  /chat-bookings/:id} : delete the "id" chatBooking.
     *
     * @param id the id of the chatBookingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatBooking(@PathVariable("id") Long id) {
        log.debug("REST request to delete ChatBooking : {}", id);
        chatBookingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
