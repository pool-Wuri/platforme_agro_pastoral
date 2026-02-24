package com.anptic.agropastoral.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class RatingRequest {

    @NotNull(message = "L'ID du producteur est obligatoire")
    private UUID productorId;

    @NotNull(message = "Le score est obligatoire")
    @Min(value = 1, message = "Le score doit être au minimum 1")
    @Max(value = 5, message = "Le score doit être au maximum 5")
    private Integer score;

    private String comment;
}
