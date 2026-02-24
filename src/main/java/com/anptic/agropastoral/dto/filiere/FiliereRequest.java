package com.anptic.agropastoral.dto.filiere;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FiliereRequest {

    @NotBlank(message = "Le nom de la filière est obligatoire")
    private String name;
}
