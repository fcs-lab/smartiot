package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.ChatSessionRepository;
import br.com.supera.smartiot.service.ChatSessionService;
import br.com.supera.smartiot.service.dto.ChatSessionDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.ChatSession}.
 */
@RestController
@RequestMapping("/api/chat-sessions")
public class ChatSessionResource {

    private final Logger log = LoggerFactory.getLogger(ChatSessionResource.class);

    private static final String ENTITY_NAME = "chatSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatSessionService chatSessionService;

    private final ChatSessionRepository chatSessionRepository;

    public ChatSessionResource(ChatSessionService chatSessionService, ChatSessionRepository chatSessionRepository) {
        this.chatSessionService = chatSessionService;
        this.chatSessionRepository = chatSessionRepository;
    }

    /**
     * {@code POST  /chat-sessions} : Create a new chatSession.
     *
     * @param chatSessionDTO the chatSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatSessionDTO, or with status {@code 400 (Bad Request)} if the chatSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChatSessionDTO> createChatSession(@Valid @RequestBody ChatSessionDTO chatSessionDTO) throws URISyntaxException {
        log.debug("REST request to save ChatSession : {}", chatSessionDTO);
        if (chatSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chatSessionDTO = chatSessionService.save(chatSessionDTO);
        return ResponseEntity.created(new URI("/api/chat-sessions/" + chatSessionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chatSessionDTO.getId().toString()))
            .body(chatSessionDTO);
    }

    /**
     * {@code PUT  /chat-sessions/:id} : Updates an existing chatSession.
     *
     * @param id the id of the chatSessionDTO to save.
     * @param chatSessionDTO the chatSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatSessionDTO,
     * or with status {@code 400 (Bad Request)} if the chatSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChatSessionDTO> updateChatSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChatSessionDTO chatSessionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChatSession : {}, {}", id, chatSessionDTO);
        if (chatSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chatSessionDTO = chatSessionService.update(chatSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatSessionDTO.getId().toString()))
            .body(chatSessionDTO);
    }

    /**
     * {@code PATCH  /chat-sessions/:id} : Partial updates given fields of an existing chatSession, field will ignore if it is null
     *
     * @param id the id of the chatSessionDTO to save.
     * @param chatSessionDTO the chatSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatSessionDTO,
     * or with status {@code 400 (Bad Request)} if the chatSessionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatSessionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChatSessionDTO> partialUpdateChatSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChatSessionDTO chatSessionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChatSession partially : {}, {}", id, chatSessionDTO);
        if (chatSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatSessionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChatSessionDTO> result = chatSessionService.partialUpdate(chatSessionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatSessionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chat-sessions} : get all the chatSessions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChatSessionDTO>> getAllChatSessions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ChatSessions");
        Page<ChatSessionDTO> page = chatSessionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chat-sessions/:id} : get the "id" chatSession.
     *
     * @param id the id of the chatSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatSessionDTO> getChatSession(@PathVariable("id") Long id) {
        log.debug("REST request to get ChatSession : {}", id);
        Optional<ChatSessionDTO> chatSessionDTO = chatSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatSessionDTO);
    }

    /**
     * {@code DELETE  /chat-sessions/:id} : delete the "id" chatSession.
     *
     * @param id the id of the chatSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatSession(@PathVariable("id") Long id) {
        log.debug("REST request to delete ChatSession : {}", id);
        chatSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
