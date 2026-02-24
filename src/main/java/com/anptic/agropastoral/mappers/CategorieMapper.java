package com.anptic.agropastoral.mappers;

import com.anptic.agropastoral.dto.categorie.CategorieRequest;
import com.anptic.agropastoral.dto.categorie.CategorieResponse;
import com.anptic.agropastoral.model.Categorie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {FiliereMapper.class})
public interface CategorieMapper {

    CategorieMapper INSTANCE = Mappers.getMapper(CategorieMapper.class);

    CategorieResponse toCategorieResponse(Categorie categorie);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "filiere", ignore = true)
    Categorie toCategorie(CategorieRequest categorieRequest);
}
