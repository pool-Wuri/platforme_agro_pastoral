package com.anptic.agropastoral.dto.categorie;

import com.anptic.agropastoral.dto.filiere.FiliereResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategorieResponse {

    private UUID id;
    private String name;
    private FiliereResponse filiere;
}
