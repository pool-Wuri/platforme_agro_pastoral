package com.anptic.agropastoral.mappers;

import com.anptic.agropastoral.dto.offer.OfferRequest;
import com.anptic.agropastoral.dto.offer.OfferResponse;
import com.anptic.agropastoral.model.Offer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class, RegionMapper.class})
public interface OfferMapper {

    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    OfferResponse toOfferResponse(Offer offer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productor", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Offer toOffer(OfferRequest offerRequest);
}
