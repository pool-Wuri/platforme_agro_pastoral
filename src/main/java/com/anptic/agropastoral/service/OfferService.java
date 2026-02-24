package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.offer.OfferRequest;
import com.anptic.agropastoral.dto.offer.OfferResponse;

import java.util.List;
import java.util.UUID;

public interface OfferService {
    OfferResponse createOffer(OfferRequest offerRequest);
    List<OfferResponse> getAllOffers();
    OfferResponse getOfferById(UUID id);
    OfferResponse updateOffer(UUID id, OfferRequest offerRequest);
    void deleteOffer(UUID id);
}
