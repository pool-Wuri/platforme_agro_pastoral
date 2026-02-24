package com.anptic.agropastoral.mappers;

import com.anptic.agropastoral.dto.region.RegionRequest;
import com.anptic.agropastoral.dto.region.RegionResponse;
import com.anptic.agropastoral.model.Region;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegionMapper {

    RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

    RegionResponse toRegionResponse(Region region);

    @Mapping(target = "id", ignore = true)
    Region toRegion(RegionRequest regionRequest);
}
