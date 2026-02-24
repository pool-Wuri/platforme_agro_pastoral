package com.anptic.agropastoral.controller;

import com.anptic.agropastoral.dto.stockmovement.StockMovementRequest;
import com.anptic.agropastoral.dto.stockmovement.StockMovementResponse;
import com.anptic.agropastoral.service.StockMovementService;
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
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
@Tag(
        name = "Mouvements de Stock",
        description = "API de gestion des mouvements de stock (entrées, sorties, transferts). " +
                "Permet de suivre tous les mouvements de produits dans les stocks avec traçabilité complète."
)
public class StockMovementController {

    private final StockMovementService stockMovementService;

    /**
     * Crée un nouveau mouvement de stock
     * Enregistre une entrée, sortie ou transfert de produit
     * Accessible aux producteurs et administrateurs
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR') or hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Créer un mouvement de stock",
            description = "Enregistre un nouveau mouvement de stock (entrée, sortie ou transfert de produit). " +
                    "Cette opération crée une trace audit complète du mouvement. " +
                    "Le mouvement peut être :\n" +
                    "- **ENTREE** : Ajout de produit au stock\n" +
                    "- **SORTIE** : Retrait de produit du stock\n" +
                    "- **TRANSFERT** : Déplacement de produit entre deux stocks\n\n" +
                    "Seuls les producteurs et administrateurs peuvent créer des mouvements. " +
                    "Le système met à jour automatiquement les quantités de stock.",
            operationId = "createStockMovement",
            tags = {"Mouvements de Stock"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Mouvement de stock créé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StockMovementResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Validation échouée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"La quantité du mouvement est obligatoire\", \"errors\": [{\"field\": \"quantity\", \"message\": \"doit être supérieur à 0\"}]}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié - Token JWT manquant ou invalide",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Unauthorized\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé - Seuls les producteurs et administrateurs peuvent créer des mouvements",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Access Denied\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock non trouvé - L'ID du stock spécifié n'existe pas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Stock not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflit - Quantité insuffisante pour une sortie ou transfert",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Quantité insuffisante en stock. Disponible: 100, Demandé: 150\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Une erreur s'est produite lors du traitement\"}")
                    )
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StockMovementResponse> createStockMovement(
            @Valid @RequestBody StockMovementRequest stockMovementRequest
    ) {
        return new ResponseEntity<>(
                stockMovementService.createStockMovement(stockMovementRequest),
                HttpStatus.CREATED
        );
    }

    /**
     * Récupère l'historique des mouvements pour un stock spécifique
     * Affiche tous les mouvements d'entrée, sortie et transfert
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/{stockId}")
    @Operation(
            summary = "Récupérer l'historique des mouvements d'un stock",
            description = "Récupère la liste complète de tous les mouvements associés à un stock spécifique. " +
                    "Cette opération retourne :\n" +
                    "- Le type de mouvement (ENTREE, SORTIE, TRANSFERT)\n" +
                    "- La quantité du mouvement\n" +
                    "- La date et l'heure du mouvement\n" +
                    "- L'utilisateur qui a effectué le mouvement\n" +
                    "- Les détails du stock source et destination (si applicable)\n\n" +
                    "Les mouvements sont triés par date décroissante (les plus récents en premier). " +
                    "Cet historique permet une traçabilité complète des produits.",
            operationId = "getStockMovements",
            tags = {"Mouvements de Stock"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Historique des mouvements récupéré avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "array",
                                    implementation = StockMovementResponse.class,
                                    example = "[" +
                                            "{\"id\": \"550e8400-e29b-41d4-a716-446655440000\", \"type\": \"ENTREE\", \"quantity\": 100, \"movementDate\": \"2026-02-24T10:30:00\"}," +
                                            "{\"id\": \"660e8400-e29b-41d4-a716-446655440000\", \"type\": \"SORTIE\", \"quantity\": 50, \"movementDate\": \"2026-02-24T09:15:00\"}" +
                                            "]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID invalide - Format UUID requis",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Invalid UUID format\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié - Token JWT manquant ou invalide",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Unauthorized\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock non trouvé - L'ID du stock spécifié n'existe pas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Stock not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Une erreur s'est produite lors de la récupération des mouvements\"}")
                    )
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<StockMovementResponse>> getStockMovements(
            @Parameter(
                    name = "stockId",
                    description = "Identifiant unique (UUID) du stock dont on souhaite consulter l'historique des mouvements. " +
                            "Le stock doit exister dans le système.",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID stockId
    ) {
        return ResponseEntity.ok(stockMovementService.getStockMovements(stockId));
    }
}
