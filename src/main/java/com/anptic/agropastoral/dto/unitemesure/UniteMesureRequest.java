package com.anptic.agropastoral.dto.unitemesure;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniteMesureRequest {

    @NotBlank(message = "Le nom de l'unité de mesure est obligatoire")
    private String name;

    @NotBlank(message = "Le symbole de l'unité de mesure est obligatoire")
    private String symbol;
}
