package com.anptic.agropastoral.mappers;

import com.anptic.agropastoral.dto.reservation.ReservationRequest;
import com.anptic.agropastoral.dto.reservation.ReservationResponse;
import com.anptic.agropastoral.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {OfferMapper.class, UserMapper.class})
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    ReservationResponse toReservationResponse(Reservation reservation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "offer", ignore = true)
    @Mapping(target = "buyer", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reservation toReservation(ReservationRequest reservationRequest);
}
