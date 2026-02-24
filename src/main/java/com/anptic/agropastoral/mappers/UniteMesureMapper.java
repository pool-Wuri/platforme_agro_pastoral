package com.anptic.agropastoral.mappers;

import com.anptic.agropastoral.dto.unitemesure.UniteMesureRequest;
import com.anptic.agropastoral.dto.unitemesure.UniteMesureResponse;
import com.anptic.agropastoral.model.UniteMesure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UniteMesureMapper {

    UniteMesureMapper INSTANCE = Mappers.getMapper(UniteMesureMapper.class);

    UniteMesureResponse toUniteMesureResponse(UniteMesure uniteMesure);

    @Mapping(target = "id", ignore = true)
    UniteMesure toUniteMesure(UniteMesureRequest uniteMesureRequest);
}
