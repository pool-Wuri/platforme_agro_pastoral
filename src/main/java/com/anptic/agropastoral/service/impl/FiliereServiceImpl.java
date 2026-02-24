package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.filiere.FiliereRequest;
import com.anptic.agropastoral.dto.filiere.FiliereResponse;
import com.anptic.agropastoral.mappers.FiliereMapper;
import com.anptic.agropastoral.model.Filiere;
import com.anptic.agropastoral.repository.FiliereRepository;
import com.anptic.agropastoral.service.FiliereService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FiliereServiceImpl implements FiliereService {

    private final FiliereRepository filiereRepository;
    private final FiliereMapper filiereMapper;

    @Override
    public FiliereResponse createFiliere(FiliereRequest filiereRequest) {
        Filiere filiere = filiereMapper.toFiliere(filiereRequest);
        return filiereMapper.toFiliereResponse(filiereRepository.save(filiere));
    }

    @Override
    public List<FiliereResponse> getAllFilieres() {
        return filiereRepository.findAll().stream()
                .map(filiereMapper::toFiliereResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FiliereResponse getFiliereById(UUID id) {
        return filiereRepository.findById(id)
                .map(filiereMapper::toFiliereResponse)
                .orElseThrow(() -> new RuntimeException("Filiere not found"));
    }

    @Override
    public FiliereResponse updateFiliere(UUID id, FiliereRequest filiereRequest) {
        Filiere filiere = filiereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filiere not found"));
        filiere.setName(filiereRequest.getName());
        return filiereMapper.toFiliereResponse(filiereRepository.save(filiere));
    }

    @Override
    public void deleteFiliere(UUID id) {
        filiereRepository.deleteById(id);
    }
}
