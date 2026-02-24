package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.region.RegionRequest;
import com.anptic.agropastoral.dto.region.RegionResponse;
import com.anptic.agropastoral.mappers.RegionMapper;
import com.anptic.agropastoral.model.Region;
import com.anptic.agropastoral.repository.RegionRepository;
import com.anptic.agropastoral.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    @Override
    public RegionResponse createRegion(RegionRequest regionRequest) {
        Region region = regionMapper.toRegion(regionRequest);
        return regionMapper.toRegionResponse(regionRepository.save(region));
    }

    @Override
    public List<RegionResponse> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(regionMapper::toRegionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RegionResponse getRegionById(UUID id) {
        return regionRepository.findById(id)
                .map(regionMapper::toRegionResponse)
                .orElseThrow(() -> new RuntimeException("Region not found"));
    }

    @Override
    public RegionResponse updateRegion(UUID id, RegionRequest regionRequest) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Region not found"));
        region.setName(regionRequest.getName());
        return regionMapper.toRegionResponse(regionRepository.save(region));
    }

    @Override
    public void deleteRegion(UUID id) {
        regionRepository.deleteById(id);
    }
}
