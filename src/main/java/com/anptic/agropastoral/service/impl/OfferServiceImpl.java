package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.offer.OfferRequest;
import com.anptic.agropastoral.dto.offer.OfferResponse;
import com.anptic.agropastoral.enums.OfferStatus;
import com.anptic.agropastoral.mappers.OfferMapper;
import com.anptic.agropastoral.model.Offer;
import com.anptic.agropastoral.model.Product;
import com.anptic.agropastoral.model.User;
import com.anptic.agropastoral.repository.OfferRepository;
import com.anptic.agropastoral.repository.ProductRepository;
import com.anptic.agropastoral.model.Region;
import com.anptic.agropastoral.repository.RegionRepository;
import com.anptic.agropastoral.repository.UserRepository;
import com.anptic.agropastoral.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    @Override
    public OfferResponse createOffer(OfferRequest offerRequest) {
        User currentUser = getCurrentUser();
        Product product = productRepository.findById(offerRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Offer offer = offerMapper.toOffer(offerRequest);
        offer.setProductor(currentUser);
        Region region = regionRepository.findById(offerRequest.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));

        offer.setProduct(product);
        offer.setRegion(region);
        offer.setStatus(OfferStatus.ACTIVE);
        offer.setCreatedAt(LocalDateTime.now());

        return offerMapper.toOfferResponse(offerRepository.save(offer));
    }

    @Override
    public List<OfferResponse> getAllOffers() {
        return offerRepository.findAll().stream()
                .map(offerMapper::toOfferResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OfferResponse getOfferById(UUID id) {
        return offerRepository.findById(id)
                .map(offerMapper::toOfferResponse)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
    }

    @Override
    public OfferResponse updateOffer(UUID id, OfferRequest offerRequest) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        Product product = productRepository.findById(offerRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Region region = regionRepository.findById(offerRequest.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));

        offer.setProduct(product);
        offer.setRegion(region);
        offer.setQuantity(offerRequest.getQuantity());
        offer.setPricePerUnit(offerRequest.getPricePerUnit());
        offer.setRegion(region);
        offer.setExpiryDate(offerRequest.getExpiryDate());

        return offerMapper.toOfferResponse(offerRepository.save(offer));
    }

    @Override
    public void deleteOffer(UUID id) {
        offerRepository.deleteById(id);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
