package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.UserReport;
import br.com.supera.smartiot.service.dto.UserReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserReport} and its DTO {@link UserReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserReportMapper extends EntityMapper<UserReportDTO, UserReport> {}
