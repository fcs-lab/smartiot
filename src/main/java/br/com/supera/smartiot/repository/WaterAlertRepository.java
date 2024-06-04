package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.WaterAlert;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WaterAlert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WaterAlertRepository extends JpaRepository<WaterAlert, Long> {}
