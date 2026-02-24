package com.anptic.agropastoral.dto.categorie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategorieRequest {

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    private String name;

    @NotNull(message = "L'ID de la filière est obligatoire")
    private UUID filiereId;
}
