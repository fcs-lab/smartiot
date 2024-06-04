package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.WaterMeasurement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WaterMeasurement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WaterMeasurementRepository extends JpaRepository<WaterMeasurement, Long> {}
