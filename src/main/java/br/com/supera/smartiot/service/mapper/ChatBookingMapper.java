package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ChatBooking;
import br.com.supera.smartiot.domain.ChatSession;
import br.com.supera.smartiot.domain.VehicleInfo;
import br.com.supera.smartiot.service.dto.ChatBookingDTO;
import br.com.supera.smartiot.service.dto.ChatSessionDTO;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ChatBooking} and its DTO {@link ChatBookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChatBookingMapper extends EntityMapper<ChatBookingDTO, ChatBooking> {
    @Mapping(target = "session", source = "session", qualifiedByName = "chatSessionId")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleInfoId")
    ChatBookingDTO toDto(ChatBooking s);

    @Named("chatSessionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChatSessionDTO toDtoChatSessionId(ChatSession chatSession);

    @Named("vehicleInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleInfoDTO toDtoVehicleInfoId(VehicleInfo vehicleInfo);
}
