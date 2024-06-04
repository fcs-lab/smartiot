package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.ChatMessage;
import br.com.supera.smartiot.repository.ChatMessageRepository;
import br.com.supera.smartiot.service.dto.ChatMessageDTO;
import br.com.supera.smartiot.service.mapper.ChatMessageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.ChatMessage}.
 */
@Service
@Transactional
public class ChatMessageService {

    private final Logger log = LoggerFactory.getLogger(ChatMessageService.class);

    private final ChatMessageRepository chatMessageRepository;

    private final ChatMessageMapper chatMessageMapper;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatMessageMapper chatMessageMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
    }

    /**
     * Save a chatMessage.
     *
     * @param chatMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatMessageDTO save(ChatMessageDTO chatMessageDTO) {
        log.debug("Request to save ChatMessage : {}", chatMessageDTO);
        ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDTO);
        chatMessage = chatMessageRepository.save(chatMessage);
        return chatMessageMapper.toDto(chatMessage);
    }

    /**
     * Update a chatMessage.
     *
     * @param chatMessageDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatMessageDTO update(ChatMessageDTO chatMessageDTO) {
        log.debug("Request to update ChatMessage : {}", chatMessageDTO);
        ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDTO);
        chatMessage = chatMessageRepository.save(chatMessage);
        return chatMessageMapper.toDto(chatMessage);
    }

    /**
     * Partially update a chatMessage.
     *
     * @param chatMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatMessageDTO> partialUpdate(ChatMessageDTO chatMessageDTO) {
        log.debug("Request to partially update ChatMessage : {}", chatMessageDTO);

        return chatMessageRepository
            .findById(chatMessageDTO.getId())
            .map(existingChatMessage -> {
                chatMessageMapper.partialUpdate(existingChatMessage, chatMessageDTO);

                return existingChatMessage;
            })
            .map(chatMessageRepository::save)
            .map(chatMessageMapper::toDto);
    }

    /**
     * Get all the chatMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatMessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatMessages");
        return chatMessageRepository.findAll(pageable).map(chatMessageMapper::toDto);
    }

    /**
     * Get one chatMessage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatMessageDTO> findOne(Long id) {
        log.debug("Request to get ChatMessage : {}", id);
        return chatMessageRepository.findById(id).map(chatMessageMapper::toDto);
    }

    /**
     * Delete the chatMessage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChatMessage : {}", id);
        chatMessageRepository.deleteById(id);
    }
}
