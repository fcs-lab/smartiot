package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.ChatUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {}
