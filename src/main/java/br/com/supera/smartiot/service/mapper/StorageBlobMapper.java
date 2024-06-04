package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.StorageBlob;
import br.com.supera.smartiot.service.dto.StorageBlobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StorageBlob} and its DTO {@link StorageBlobDTO}.
 */
@Mapper(componentModel = "spring")
public interface StorageBlobMapper extends EntityMapper<StorageBlobDTO, StorageBlob> {}
