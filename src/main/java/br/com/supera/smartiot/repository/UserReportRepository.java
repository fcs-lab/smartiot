package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.UserReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {}
