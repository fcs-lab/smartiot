package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.ChatBooking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChatBooking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatBookingRepository extends JpaRepository<ChatBooking, Long> {}
