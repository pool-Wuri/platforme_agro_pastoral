package com.anptic.agropastoral.service;

import com.anptic.agropastoral.dto.unitemesure.UniteMesureRequest;
import com.anptic.agropastoral.dto.unitemesure.UniteMesureResponse;

import java.util.List;
import java.util.UUID;

public interface UniteMesureService {
    UniteMesureResponse createUniteMesure(UniteMesureRequest uniteMesureRequest);
    List<UniteMesureResponse> getAllUniteMesures();
    UniteMesureResponse getUniteMesureById(UUID id);
    UniteMesureResponse updateUniteMesure(UUID id, UniteMesureRequest uniteMesureRequest);
    void deleteUniteMesure(UUID id);
}
