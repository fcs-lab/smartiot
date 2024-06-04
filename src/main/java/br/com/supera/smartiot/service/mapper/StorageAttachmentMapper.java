package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.StorageAttachment;
import br.com.supera.smartiot.service.dto.StorageAttachmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StorageAttachment} and its DTO {@link StorageAttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface StorageAttachmentMapper extends EntityMapper<StorageAttachmentDTO, StorageAttachment> {}
