package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.filiere.FiliereRequest;
import com.anptic.agropastoral.dto.filiere.FiliereResponse;

import java.util.List;
import java.util.UUID;

public interface FiliereService {
    FiliereResponse createFiliere(FiliereRequest filiereRequest);
    List<FiliereResponse> getAllFilieres();
    FiliereResponse getFiliereById(UUID id);
    FiliereResponse updateFiliere(UUID id, FiliereRequest filiereRequest);
    void deleteFiliere(UUID id);
}
