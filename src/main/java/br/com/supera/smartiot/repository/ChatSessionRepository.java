package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.ChatSession;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatSession entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {}
