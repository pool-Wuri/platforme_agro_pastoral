package com.anptic.agropastoral.mappers;

import com.anptic.agropastoral.dto.filiere.FiliereRequest;
import com.anptic.agropastoral.dto.filiere.FiliereResponse;
import com.anptic.agropastoral.model.Filiere;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FiliereMapper {

    FiliereMapper INSTANCE = Mappers.getMapper(FiliereMapper.class);

    FiliereResponse toFiliereResponse(Filiere filiere);

    @Mapping(target = "id", ignore = true)
    Filiere toFiliere(FiliereRequest filiereRequest);
}
