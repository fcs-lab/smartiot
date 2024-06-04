package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.VehicleInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleInfoRepository extends JpaRepository<VehicleInfo, Long> {}
