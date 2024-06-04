package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.SystemAlert;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemAlert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemAlertRepository extends JpaRepository<SystemAlert, Long> {}
