package com.anptic.agropastoral.repository;

import com.anptic.agropastoral.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, UUID> {
}
