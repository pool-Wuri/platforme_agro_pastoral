package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.region.RegionRequest;
import com.anptic.agropastoral.dto.region.RegionResponse;

import java.util.List;
import java.util.UUID;

public interface RegionService {
    RegionResponse createRegion(RegionRequest regionRequest);
    List<RegionResponse> getAllRegions();
    RegionResponse getRegionById(UUID id);
    RegionResponse updateRegion(UUID id, RegionRequest regionRequest);
    void deleteRegion(UUID id);
}
