package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.WaterSensor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WaterSensor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WaterSensorRepository extends JpaRepository<WaterSensor, Long> {}
