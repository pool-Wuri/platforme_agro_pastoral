package com.anptic.agropastoral.controller;

import com.anptic.agropastoral.dto.categorie.CategorieRequest;
import com.anptic.agropastoral.dto.categorie.CategorieResponse;
import com.anptic.agropastoral.service.CategorieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    @PostMapping
    @PreAuthorize("hasRole(\'ROLE_ADMIN\')")
    public ResponseEntity<CategorieResponse> createCategorie(@Valid @RequestBody CategorieRequest categorieRequest) {
        return new ResponseEntity<>(categorieService.createCategorie(categorieRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategorieResponse>> getAllCategories() {
        return ResponseEntity.ok(categorieService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategorieResponse> getCategorieById(@PathVariable UUID id) {
        return ResponseEntity.ok(categorieService.getCategorieById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole(\'ROLE_ADMIN\')")
    public ResponseEntity<CategorieResponse> updateCategorie(@PathVariable UUID id, @Valid @RequestBody CategorieRequest categorieRequest) {
        return ResponseEntity.ok(categorieService.updateCategorie(id, categorieRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\'ROLE_ADMIN\')")
    public ResponseEntity<Void> deleteCategorie(@PathVariable UUID id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.noContent().build();
    }
}
