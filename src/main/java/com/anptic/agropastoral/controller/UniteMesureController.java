package com.anptic.agropastoral.controller;

import com.anptic.agropastoral.dto.unitemesure.UniteMesureRequest;
import com.anptic.agropastoral.dto.unitemesure.UniteMesureResponse;
import com.anptic.agropastoral.service.UniteMesureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/unite-mesures")
@RequiredArgsConstructor
@Tag(
        name = "Unités de Mesure",
        description = "API de gestion des unités de mesure utilisées pour les produits agricoles"
)
public class UniteMesureController {

    private final UniteMesureService uniteMesureService;

    /**
     * Crée une nouvelle unité de mesure
     * Seuls les administrateurs peuvent créer des unités de mesure
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Créer une nouvelle unité de mesure",
            description = "Crée une nouvelle unité de mesure dans le système. Cette opération est réservée aux administrateurs. " +
                    "L'unité de mesure doit avoir un nom unique et un symbole pour l'identifier.",
            operationId = "createUniteMesure",
            tags = {"Unités de Mesure"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Unité de mesure créée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UniteMesureResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Validation échouée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Le nom de l'unité est obligatoire\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié - Token JWT manquant ou invalide",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé - Seuls les administrateurs peuvent créer des unités de mesure",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(mediaType = "application/json")
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UniteMesureResponse> createUniteMesure(
            @Valid @RequestBody UniteMesureRequest uniteMesureRequest
    ) {
        return new ResponseEntity<>(
                uniteMesureService.createUniteMesure(uniteMesureRequest),
                HttpStatus.CREATED
        );
    }

    /**
     * Récupère toutes les unités de mesure
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping
    @Operation(
            summary = "Récupérer toutes les unités de mesure",
            description = "Récupère la liste complète de toutes les unités de mesure disponibles dans le système. " +
                    "Cette opération est accessible à tous les utilisateurs authentifiés. " +
                    "Les résultats incluent l'ID, le nom et le symbole de chaque unité.",
            operationId = "getAllUniteMesures",
            tags = {"Unités de Mesure"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des unités de mesure récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UniteMesureResponse.class, type = "array")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié - Token JWT manquant ou invalide",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(mediaType = "application/json")
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<UniteMesureResponse>> getAllUniteMesures() {
        return ResponseEntity.ok(uniteMesureService.getAllUniteMesures());
    }

    /**
     * Récupère une unité de mesure par son ID
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer une unité de mesure par ID",
            description = "Récupère les détails d'une unité de mesure spécifique en utilisant son identifiant unique (UUID). " +
                    "Cette opération retourne le nom, le symbole et les autres propriétés de l'unité.",
            operationId = "getUniteMesureById",
            tags = {"Unités de Mesure"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Unité de mesure récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UniteMesureResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID invalide - Format UUID requis",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié - Token JWT manquant ou invalide",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Unité de mesure non trouvée - L'ID spécifié n'existe pas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"UniteMesure not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(mediaType = "application/json")
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UniteMesureResponse> getUniteMesureById(
            @Parameter(
                    name = "id",
                    description = "Identifiant unique (UUID) de l'unité de mesure à récupérer",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(uniteMesureService.getUniteMesureById(id));
    }

    /**
     * Met à jour une unité de mesure existante
     * Seuls les administrateurs peuvent modifier les unités de mesure
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Mettre à jour une unité de mesure",
            description = "Met à jour les informations d'une unité de mesure existante. Cette opération est réservée aux administrateurs. " +
                    "Vous pouvez modifier le nom et le symbole de l'unité. L'ID ne peut pas être modifié.",
            operationId = "updateUniteMesure",
            tags = {"Unités de Mesure"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Unité de mesure mise à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UniteMesureResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Validation échouée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Le symbole de l'unité est obligatoire\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié - Token JWT manquant ou invalide",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé - Seuls les administrateurs peuvent modifier les unités de mesure",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Unité de mesure non trouvée - L'ID spécifié n'existe pas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"UniteMesure not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(mediaType = "application/json")
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UniteMesureResponse> updateUniteMesure(
            @Parameter(
                    name = "id",
                    description = "Identifiant unique (UUID) de l'unité de mesure à mettre à jour",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID id,
            @Valid @RequestBody UniteMesureRequest uniteMesureRequest
    ) {
        return ResponseEntity.ok(uniteMesureService.updateUniteMesure(id, uniteMesureRequest));
    }

    /**
     * Supprime une unité de mesure
     * Seuls les administrateurs peuvent supprimer les unités de mesure
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Supprimer une unité de mesure",
            description = "Supprime une unité de mesure du système. Cette opération est réservée aux administrateurs. " +
                    "Attention : la suppression est définitive et ne peut pas être annulée. " +
                    "Assurez-vous que l'unité n'est pas utilisée par des produits avant de la supprimer.",
            operationId = "deleteUniteMesure",
            tags = {"Unités de Mesure"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Unité de mesure supprimée avec succès",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié - Token JWT manquant ou invalide",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé - Seuls les administrateurs peuvent supprimer les unités de mesure",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Unité de mesure non trouvée - L'ID spécifié n'existe pas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"UniteMesure not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(mediaType = "application/json")
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteUniteMesure(
            @Parameter(
                    name = "id",
                    description = "Identifiant unique (UUID) de l'unité de mesure à supprimer",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID id
    ) {
        uniteMesureService.deleteUniteMesure(id);
        return ResponseEntity.noContent().build();
    }
}
