package com.anptic.agropastoral.dto.region;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionRequest {

    @NotBlank(message = "Le nom de la région est obligatoire")
    private String name;
}
