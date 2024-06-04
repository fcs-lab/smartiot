package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.ChatBooking;
import br.com.supera.smartiot.repository.ChatBookingRepository;
import br.com.supera.smartiot.service.dto.ChatBookingDTO;
import br.com.supera.smartiot.service.mapper.ChatBookingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.ChatBooking}.
 */
@Service
@Transactional
public class ChatBookingService {

    private final Logger log = LoggerFactory.getLogger(ChatBookingService.class);

    private final ChatBookingRepository chatBookingRepository;

    private final ChatBookingMapper chatBookingMapper;

    public ChatBookingService(ChatBookingRepository chatBookingRepository, ChatBookingMapper chatBookingMapper) {
        this.chatBookingRepository = chatBookingRepository;
        this.chatBookingMapper = chatBookingMapper;
    }

    /**
     * Save a chatBooking.
     *
     * @param chatBookingDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatBookingDTO save(ChatBookingDTO chatBookingDTO) {
        log.debug("Request to save ChatBooking : {}", chatBookingDTO);
        ChatBooking chatBooking = chatBookingMapper.toEntity(chatBookingDTO);
        chatBooking = chatBookingRepository.save(chatBooking);
        return chatBookingMapper.toDto(chatBooking);
    }

    /**
     * Update a chatBooking.
     *
     * @param chatBookingDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatBookingDTO update(ChatBookingDTO chatBookingDTO) {
        log.debug("Request to update ChatBooking : {}", chatBookingDTO);
        ChatBooking chatBooking = chatBookingMapper.toEntity(chatBookingDTO);
        chatBooking = chatBookingRepository.save(chatBooking);
        return chatBookingMapper.toDto(chatBooking);
    }

    /**
     * Partially update a chatBooking.
     *
     * @param chatBookingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatBookingDTO> partialUpdate(ChatBookingDTO chatBookingDTO) {
        log.debug("Request to partially update ChatBooking : {}", chatBookingDTO);

        return chatBookingRepository
            .findById(chatBookingDTO.getId())
            .map(existingChatBooking -> {
                chatBookingMapper.partialUpdate(existingChatBooking, chatBookingDTO);

                return existingChatBooking;
            })
            .map(chatBookingRepository::save)
            .map(chatBookingMapper::toDto);
    }

    /**
     * Get all the chatBookings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatBookingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatBookings");
        return chatBookingRepository.findAll(pageable).map(chatBookingMapper::toDto);
    }

    /**
     * Get one chatBooking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatBookingDTO> findOne(Long id) {
        log.debug("Request to get ChatBooking : {}", id);
        return chatBookingRepository.findById(id).map(chatBookingMapper::toDto);
    }

    /**
     * Delete the chatBooking by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChatBooking : {}", id);
        chatBookingRepository.deleteById(id);
    }
}
