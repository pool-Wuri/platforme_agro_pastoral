package com.anptic.agropastoral.service.impl;

import com.anptic.agropastoral.dto.unitemesure.UniteMesureRequest;
import com.anptic.agropastoral.dto.unitemesure.UniteMesureResponse;
import com.anptic.agropastoral.mappers.UniteMesureMapper;
import com.anptic.agropastoral.model.UniteMesure;
import com.anptic.agropastoral.repository.UniteMesureRepository;
import com.anptic.agropastoral.service.UniteMesureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniteMesureServiceImpl implements UniteMesureService {

    private final UniteMesureRepository uniteMesureRepository;
    private final UniteMesureMapper uniteMesureMapper;

    @Override
    public UniteMesureResponse createUniteMesure(UniteMesureRequest uniteMesureRequest) {
        UniteMesure uniteMesure = uniteMesureMapper.toUniteMesure(uniteMesureRequest);
        return uniteMesureMapper.toUniteMesureResponse(uniteMesureRepository.save(uniteMesure));
    }

    @Override
    public List<UniteMesureResponse> getAllUniteMesures() {
        return uniteMesureRepository.findAll().stream()
                .map(uniteMesureMapper::toUniteMesureResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UniteMesureResponse getUniteMesureById(UUID id) {
        return uniteMesureRepository.findById(id)
                .map(uniteMesureMapper::toUniteMesureResponse)
                .orElseThrow(() -> new RuntimeException("UniteMesure not found"));
    }

    @Override
    public UniteMesureResponse updateUniteMesure(UUID id, UniteMesureRequest uniteMesureRequest) {
        UniteMesure uniteMesure = uniteMesureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UniteMesure not found"));
        uniteMesure.setName(uniteMesureRequest.getName());
        uniteMesure.setSymbol(uniteMesureRequest.getSymbol());
        return uniteMesureMapper.toUniteMesureResponse(uniteMesureRepository.save(uniteMesure));
    }

    @Override
    public void deleteUniteMesure(UUID id) {
        uniteMesureRepository.deleteById(id);
    }
}
