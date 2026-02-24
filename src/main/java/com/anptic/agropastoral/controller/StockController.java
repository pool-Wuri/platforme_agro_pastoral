package com.anptic.agropastoral.controller;

import com.anptic.agropastoral.dto.stock.StockRequest;
import com.anptic.agropastoral.dto.stock.StockResponse;
import com.anptic.agropastoral.service.StockService;
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
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Tag(
        name = "Gestion des Stocks",
        description = "API de gestion complète des stocks agricoles. Permet aux producteurs de créer, consulter et gérer " +
                "leurs stocks de produits par région. Chaque stock est associé à un produit, une quantité et une région spécifique."
)
public class StockController {

    private final StockService stockService;

    /**
     * Crée un nouveau stock pour le producteur connecté
     * Le stock est automatiquement associé à l'utilisateur courant
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR')")
    @Operation(
            summary = "Créer un nouveau stock",
            description = "Crée un nouveau stock pour le producteur connecté. Le stock est automatiquement associé à l'utilisateur courant. " +
                    "Vous devez spécifier :\n" +
                    "- **productId** : L'ID du produit à stocker\n" +
                    "- **quantity** : La quantité initiale du stock (doit être > 0)\n" +
                    "- **regionId** : L'ID de la région où le stock est situé\n\n" +
                    "Le système initialise automatiquement les dates de création et de dernière mise à jour. " +
                    "Seuls les producteurs peuvent créer des stocks.",
            operationId = "createStock",
            tags = {"Gestion des Stocks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Stock créé avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StockResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Validation échouée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"La quantité du stock est obligatoire\", \"errors\": [{\"field\": \"quantity\", \"message\": \"doit être supérieur à 0\"}]}")
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
                    description = "Accès refusé - Seuls les producteurs peuvent créer des stocks",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Access Denied - Only ROLE_PRODUCTEUR can create stocks\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Produit ou région non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Product not found or Region not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Une erreur s'est produite lors de la création du stock\"}")
                    )
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StockResponse> createStock(
            @Valid @RequestBody StockRequest stockRequest
    ) {
        return new ResponseEntity<>(
                stockService.createStock(stockRequest),
                HttpStatus.CREATED
        );
    }

    /**
     * Récupère tous les stocks du producteur connecté
     * Retourne uniquement les stocks appartenant à l'utilisateur courant
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR')")
    @Operation(
            summary = "Récupérer tous mes stocks",
            description = "Récupère la liste complète de tous les stocks appartenant au producteur connecté. " +
                    "Cette opération retourne :\n" +
                    "- L'ID du stock\n" +
                    "- Les détails du produit stocké\n" +
                    "- La quantité disponible\n" +
                    "- La région du stock\n" +
                    "- Les dates de création et dernière mise à jour\n\n" +
                    "Les résultats incluent uniquement les stocks de l'utilisateur connecté. " +
                    "Les administrateurs ne peuvent pas accéder à cet endpoint.",
            operationId = "getUserStocks",
            tags = {"Gestion des Stocks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des stocks récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "array",
                                    implementation = StockResponse.class
                            )
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
                    description = "Accès refusé - Seuls les producteurs peuvent consulter leurs stocks",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Access Denied - Only ROLE_PRODUCTEUR can access\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Une erreur s'est produite lors de la récupération des stocks\"}")
                    )
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<StockResponse>> getUserStocks() {
        return ResponseEntity.ok(stockService.getUserStocks());
    }

    /**
     * Récupère un stock spécifique par son ID
     * Accessible à tous les utilisateurs authentifiés
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer un stock par ID",
            description = "Récupère les détails complets d'un stock spécifique en utilisant son identifiant unique (UUID). " +
                    "Cette opération retourne :\n" +
                    "- Les informations du produit stocké\n" +
                    "- La quantité disponible\n" +
                    "- La région du stock\n" +
                    "- L'utilisateur propriétaire du stock\n" +
                    "- Les dates de création et dernière mise à jour\n\n" +
                    "Cet endpoint est accessible à tous les utilisateurs authentifiés pour consulter les stocks publics.",
            operationId = "getStockById",
            tags = {"Gestion des Stocks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock récupéré avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StockResponse.class)
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
                    description = "Stock non trouvé - L'ID spécifié n'existe pas",
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
                            schema = @Schema(example = "{\"message\": \"Une erreur s'est produite lors de la récupération du stock\"}")
                    )
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StockResponse> getStockById(
            @Parameter(
                    name = "id",
                    description = "Identifiant unique (UUID) du stock à récupérer. " +
                            "Le stock doit exister dans le système.",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    /**
     * Met à jour un stock existant
     * Permet de modifier la quantité et la région du stock
     * Accessible aux producteurs et administrateurs
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR') or hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Mettre à jour un stock",
            description = "Met à jour les informations d'un stock existant. Vous pouvez modifier :\n" +
                    "- **productId** : Le produit stocké (référence à un autre produit)\n" +
                    "- **quantity** : La quantité disponible (doit être > 0)\n" +
                    "- **regionId** : La région du stock\n\n" +
                    "La date de dernière mise à jour est automatiquement mise à jour. " +
                    "Les producteurs ne peuvent modifier que leurs propres stocks. " +
                    "Les administrateurs peuvent modifier tous les stocks.",
            operationId = "updateStock",
            tags = {"Gestion des Stocks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Stock mis à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StockResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requête invalide - Validation échouée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"La quantité du stock doit être supérieur à 0\"}")
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
                    description = "Accès refusé - Vous ne pouvez modifier que vos propres stocks",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Access Denied - You can only modify your own stocks\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock, produit ou région non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Stock not found or Product not found or Region not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Une erreur s'est produite lors de la mise à jour du stock\"}")
                    )
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<StockResponse> updateStock(
            @Parameter(
                    name = "id",
                    description = "Identifiant unique (UUID) du stock à mettre à jour. " +
                            "Le stock doit exister et appartenir à l'utilisateur connecté (sauf pour les administrateurs).",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID id,
            @Valid @RequestBody StockRequest stockRequest
    ) {
        return ResponseEntity.ok(stockService.updateStock(id, stockRequest));
    }

    /**
     * Supprime un stock
     * Accessible aux producteurs et administrateurs
     * La suppression est définitive
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PRODUCTEUR') or hasRole('ROLE_ADMIN')")
    @Operation(
            summary = "Supprimer un stock",
            description = "Supprime un stock du système. Cette opération est définitive et ne peut pas être annulée. " +
                    "Avant de supprimer un stock, assurez-vous que :\n" +
                    "- Aucune offre active n'est associée à ce stock\n" +
                    "- Aucune réservation n'est en cours\n" +
                    "- Vous avez une sauvegarde des données si nécessaire\n\n" +
                    "Les producteurs ne peuvent supprimer que leurs propres stocks. " +
                    "Les administrateurs peuvent supprimer tous les stocks. " +
                    "L'historique des mouvements de stock est conservé pour la traçabilité.",
            operationId = "deleteStock",
            tags = {"Gestion des Stocks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Stock supprimé avec succès",
                    content = @Content()
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
                    description = "Accès refusé - Vous ne pouvez supprimer que vos propres stocks",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Access Denied - You can only delete your own stocks\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Stock non trouvé - L'ID spécifié n'existe pas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Stock not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflit - Le stock ne peut pas être supprimé (offres actives ou réservations en cours)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Cannot delete stock with active offers or reservations\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur serveur interne",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Une erreur s'est produite lors de la suppression du stock\"}")
                    )
            )
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Void> deleteStock(
            @Parameter(
                    name = "id",
                    description = "Identifiant unique (UUID) du stock à supprimer. " +
                            "Le stock doit exister et appartenir à l'utilisateur connecté (sauf pour les administrateurs). " +
                            "ATTENTION : Cette opération est définitive.",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID id
    ) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }
}
