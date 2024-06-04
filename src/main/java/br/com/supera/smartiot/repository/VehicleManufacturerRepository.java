package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.VehicleManufacturer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleManufacturer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleManufacturerRepository extends JpaRepository<VehicleManufacturer, Long> {}
