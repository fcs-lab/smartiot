package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.UserDashboard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserDashboard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDashboardRepository extends JpaRepository<UserDashboard, Long> {}
