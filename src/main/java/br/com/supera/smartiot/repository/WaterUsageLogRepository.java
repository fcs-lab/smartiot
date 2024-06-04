package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.WaterUsageLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WaterUsageLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WaterUsageLogRepository extends JpaRepository<WaterUsageLog, Long> {}
