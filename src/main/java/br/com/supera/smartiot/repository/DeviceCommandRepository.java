package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.DeviceCommand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeviceCommand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceCommandRepository extends JpaRepository<DeviceCommand, Long> {}
