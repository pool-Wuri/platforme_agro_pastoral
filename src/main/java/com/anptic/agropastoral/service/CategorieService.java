package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.categorie.CategorieRequest;
import com.anptic.agropastoral.dto.categorie.CategorieResponse;

import java.util.List;
import java.util.UUID;

public interface CategorieService {
    CategorieResponse createCategorie(CategorieRequest categorieRequest);
    List<CategorieResponse> getAllCategories();
    CategorieResponse getCategorieById(UUID id);
    CategorieResponse updateCategorie(UUID id, CategorieRequest categorieRequest);
    void deleteCategorie(UUID id);
}
