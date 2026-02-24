# Backend SUIVI_AGRO_PASTORAL

## Description

Ce backend Spring Boot implémente une plateforme complète de gestion des stocks pour les agro-pastoraux et petits industriels, incluant un marché virtuel pour les réservations d'achat.

## Fonctionnalités

### Authentification et Autorisation
- Authentification JWT sécurisée
- Gestion des rôles : ADMIN, PRODUCTEUR, ACHETEUR, AUTORITE
- Contrôle d'accès basé sur les rôles (RBAC)

### Gestion des Stocks
- Création et gestion des stocks
- Suivi des mouvements de stock (entrées, sorties, transferts)
- Historique complet des mouvements

### Marché Virtuel
- Publication d'offres de produits
- Système de réservation avec gestion des quantités
- Validation/rejet des réservations
- Notation des vendeurs
- Paiement via Faso Arzeka

### Administration
- Gestion des utilisateurs
- Gestion des produits et catégories
- Rapports et statistiques
- Logs d'audit

## Technologies

- **Framework** : Spring Boot 3.2.3
- **Langage** : Java 17
- **Base de données** : PostgreSQL 16
- **Sécurité** : Spring Security + JWT
- **ORM** : JPA/Hibernate
- **Mapping** : MapStruct
- **Documentation** : Swagger/OpenAPI 3.0
- **Build** : Maven

## Prérequis

- Java 17 ou supérieur
- PostgreSQL 16
- Maven 3.6+

## Installation

### 1. Cloner le projet

```bash
git clone <repository-url>
cd agro-pastoral-backend
```

### 2. Configurer la base de données

Créer une base de données PostgreSQL :

```sql
CREATE DATABASE agropastoral;
```

### 3. Configurer l'application

Éditer le fichier `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/agropastoral
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

jwt.secret=your-super-secret-key-that-is-long-enough-to-be-secure
jwt.expiration=86400000
```

### 4. Compiler et exécuter

```bash
mvn clean install
mvn spring-boot:run
```

L'application sera accessible à `http://localhost:8080`

## Documentation API

La documentation Swagger est disponible à :
- **URL** : `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON** : `http://localhost:8080/api-docs`

## Endpoints Principaux

### Authentification
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion

### Produits
- `GET /api/products` - Lister les produits
- `POST /api/products` - Créer un produit (ADMIN)
- `GET /api/products/{id}` - Détail d'un produit
- `PUT /api/products/{id}` - Modifier un produit (ADMIN)
- `DELETE /api/products/{id}` - Supprimer un produit (ADMIN)

### Stocks
- `GET /api/stocks` - Lister mes stocks (PRODUCTEUR)
- `POST /api/stocks` - Créer un stock (PRODUCTEUR)
- `GET /api/stocks/{id}` - Détail d'un stock
- `PUT /api/stocks/{id}` - Modifier un stock
- `DELETE /api/stocks/{id}` - Supprimer un stock

### Mouvements de Stock
- `POST /api/stock-movements` - Créer un mouvement
- `GET /api/stock-movements/{stockId}` - Historique des mouvements

### Offres (Marché Virtuel)
- `GET /api/offers` - Lister les offres
- `POST /api/offers` - Créer une offre (PRODUCTEUR)
- `GET /api/offers/{id}` - Détail d'une offre
- `PUT /api/offers/{id}` - Modifier une offre
- `DELETE /api/offers/{id}` - Supprimer une offre

### Réservations
- `POST /api/reservations` - Créer une réservation (ACHETEUR)
- `GET /api/reservations` - Lister mes réservations
- `GET /api/reservations/{id}` - Détail d'une réservation
- `PUT /api/reservations/{id}/confirm` - Confirmer une réservation (PRODUCTEUR)
- `PUT /api/reservations/{id}/reject` - Rejeter une réservation (PRODUCTEUR)
- `DELETE /api/reservations/{id}` - Annuler une réservation

## Structure du Projet

```
src/main/java/com/anptic/agropastoral/
├── config/              # Configuration Spring
├── controller/          # Contrôleurs REST
├── dto/                 # Data Transfer Objects
├── exception/           # Gestion des exceptions
├── mappers/             # Mappers MapStruct
├── model/               # Entités JPA
├── repository/          # Repositories JPA
└── service/             # Services métier
```

## Sécurité

### Authentification JWT

Tous les endpoints sauf `/api/auth/**` et `/swagger-ui/**` nécessitent un token JWT valide.

Pour accéder aux endpoints protégés, inclure le header :

```
Authorization: Bearer <token>
```

### Rôles et Permissions

| Rôle | Permissions |
|------|-------------|
| ROLE_ADMIN | Gestion complète de la plateforme |
| ROLE_PRODUCTEUR | Gestion des stocks et offres |
| ROLE_ACHETEUR | Consultation des offres et réservations |
| ROLE_AUTORITE | Consultation des statistiques |

## Logs d'Audit

Tous les changements sont enregistrés dans la table `audit_logs` avec :
- ID de l'utilisateur
- Type d'action
- Type d'entité modifiée
- ID de l'entité
- Changements effectués
- Timestamp
- Adresse IP

## Gestion des Erreurs

Les erreurs sont gérées globalement par `GlobalExceptionHandler` et retournent des réponses structurées :

```json
{
  "timestamp": "2026-02-24T10:30:00",
  "status": 400,
  "errors": {
    "email": "L'email doit être valide"
  }
}
```

## Performance

- Temps de réponse cible : 100-200 ms
- Utilisateurs simultanés supportés : 100+
- Disponibilité : 99.9%

## Maintenance

### Sauvegardes

Les sauvegardes de la base de données doivent être effectuées régulièrement :

```bash
pg_dump agropastoral > backup.sql
```

### Monitoring

L'application expose des métriques via Spring Boot Actuator :
- `http://localhost:8080/actuator/health`
- `http://localhost:8080/actuator/metrics`

## Support

Pour toute question ou problème, veuillez contacter l'équipe de développement.

## Licence

Tous droits réservés - ANPTIC 2026
# platforme_agro_pastoral
