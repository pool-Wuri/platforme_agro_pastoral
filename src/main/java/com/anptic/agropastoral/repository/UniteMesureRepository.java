package com.anptic.agropastoral.repository;

import com.anptic.agropastoral.model.UniteMesure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UniteMesureRepository extends JpaRepository<UniteMesure, UUID> {
}
