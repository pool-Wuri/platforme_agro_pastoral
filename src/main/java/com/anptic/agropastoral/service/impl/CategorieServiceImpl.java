package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.categorie.CategorieRequest;
import com.anptic.agropastoral.dto.categorie.CategorieResponse;
import com.anptic.agropastoral.mappers.CategorieMapper;
import com.anptic.agropastoral.model.Categorie;
import com.anptic.agropastoral.model.Filiere;
import com.anptic.agropastoral.repository.CategorieRepository;
import com.anptic.agropastoral.repository.FiliereRepository;
import com.anptic.agropastoral.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;
    private final CategorieMapper categorieMapper;
    private final FiliereRepository filiereRepository;

    @Override
    public CategorieResponse createCategorie(CategorieRequest categorieRequest) {
        Filiere filiere = filiereRepository.findById(categorieRequest.getFiliereId())
                .orElseThrow(() -> new RuntimeException("Filiere not found"));
        Categorie categorie = categorieMapper.toCategorie(categorieRequest);
        categorie.setFiliere(filiere);
        return categorieMapper.toCategorieResponse(categorieRepository.save(categorie));
    }

    @Override
    public List<CategorieResponse> getAllCategories() {
        return categorieRepository.findAll().stream()
                .map(categorieMapper::toCategorieResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategorieResponse getCategorieById(UUID id) {
        return categorieRepository.findById(id)
                .map(categorieMapper::toCategorieResponse)
                .orElseThrow(() -> new RuntimeException("Categorie not found"));
    }

    @Override
    public CategorieResponse updateCategorie(UUID id, CategorieRequest categorieRequest) {
        Categorie categorie = categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categorie not found"));
        Filiere filiere = filiereRepository.findById(categorieRequest.getFiliereId())
                .orElseThrow(() -> new RuntimeException("Filiere not found"));

        categorie.setName(categorieRequest.getName());
        categorie.setFiliere(filiere);

        return categorieMapper.toCategorieResponse(categorieRepository.save(categorie));
    }

    @Override
    public void deleteCategorie(UUID id) {
        categorieRepository.deleteById(id);
    }
}
