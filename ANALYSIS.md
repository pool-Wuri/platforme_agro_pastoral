# Analyse du Cahier de Charges - Plateforme SUIVI_AGRO_PASTORAL

## 1. Entités Principales Identifiées

### Utilisateurs et Rôles
- **ROLE_ADMIN** : Administrateurs de la plateforme
- **ROLE_PRODUCTEUR** : Producteurs agro-pastoraux, petits industriels, transformateurs
- **ROLE_ACHETEUR** : Grossistes, commerçants, institutions
- **ROLE_AUTORITE** : Autorités publiques (consultation statistiques)

### Entités Métier

#### 1. **User (Utilisateur)**
- id (UUID)
- email (unique)
- password (hashé)
- firstName, lastName
- phoneNumber
- role (enum: ADMIN, PRODUCTEUR, ACHETEUR, AUTORITE)
- status (ACTIVE, INACTIVE, PENDING_VALIDATION)
- kyc_status (KYC simplifiée)
- createdAt, updatedAt
- lastLogin

#### 2. **Product (Produit)**
- id (UUID)
- name
- description
- category (filière: céréales, légumineuses, etc.)
- unit (kg, litre, etc.)
- createdAt, updatedAt

#### 3. **Stock**
- id (UUID)
- userId (producteur)
- productId
- quantity (quantité disponible)
- region
- lastUpdated
- createdAt

#### 4. **StockMovement (Mouvement de Stock)**
- id (UUID)
- stockId
- type (ENTRY, EXIT, TRANSFER)
- quantity
- reason
- previousQuantity
- newQuantity
- timestamp
- userId (qui a effectué le mouvement)

#### 5. **Offer (Offre - Marché Virtuel)**
- id (UUID)
- productorId (vendeur)
- productId
- quantity
- pricePerUnit
- region
- status (ACTIVE, RESERVED, SOLD, CANCELLED)
- createdAt
- expiryDate

#### 6. **Reservation (Réservation)**
- id (UUID)
- offerId
- buyerId
- quantity
- reservedUntil (durée de réservation)
- status (PENDING, CONFIRMED, REJECTED, CANCELLED)
- createdAt
- updatedAt

#### 7. **Payment (Paiement)**
- id (UUID)
- reservationId
- amount
- paymentMethod (FASO_ARZEKA)
- status (PENDING, SUCCESS, FAILED)
- transactionId
- timestamp

#### 8. **Rating (Notation)**
- id (UUID)
- productorId (vendeur noté)
- buyerId (acheteur qui note)
- score (1-5)
- comment
- createdAt

#### 9. **Notification**
- id (UUID)
- userId
- type (SMS, EMAIL, IN_APP)
- message
- isRead
- createdAt

#### 10. **AuditLog**
- id (UUID)
- userId
- action
- entityType
- entityId
- changes
- timestamp
- ipAddress

## 2. Fonctionnalités Principales

### Gestion des Comptes
- ✅ Inscription en ligne (web/mobile)
- ✅ Validation par l'administration
- ✅ Authentification sécurisée (JWT)
- ✅ Gestion des rôles et permissions

### Gestion des Stocks
- ✅ Approvisionnement (entrées)
- ✅ Ventes (sorties)
- ✅ Transferts de produits
- ✅ Suivi des quantités disponibles
- ✅ Alertes de seuil bas
- ✅ Historique des mouvements
- ✅ Inventaire
- ✅ Import Excel/formulaire

### Tableau de Bord
- ✅ Visualisation par région, filière, type d'acteur
- ✅ Évolution des stocks
- ✅ Zones en tension
- ✅ Recherche multicritères
- ✅ Export Excel

### Marché Virtuel
- ✅ Publication automatique des offres
- ✅ Consultation avec recherche multicritères
- ✅ Réservation (quantité + durée)
- ✅ Notifications SMS
- ✅ Validation/rejet de réservation
- ✅ Discussion acheteur-vendeur
- ✅ Notation vendeurs
- ✅ Paiement via Faso Arzeka

### Administration
- ✅ Gestion des utilisateurs
- ✅ Modération des offres
- ✅ Gestion des produits/catégories
- ✅ Rapports statistiques
- ✅ Export de données

## 3. Endpoints API Principaux

### Authentication
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion
- `POST /api/auth/refresh` - Refresh token
- `POST /api/auth/logout` - Déconnexion

### Users
- `GET /api/users` - Lister (ADMIN)
- `GET /api/users/{id}` - Détail
- `PUT /api/users/{id}` - Modifier
- `DELETE /api/users/{id}` - Supprimer (ADMIN)
- `GET /api/users/{id}/profile` - Profil utilisateur

### Products
- `GET /api/products` - Lister
- `POST /api/products` - Créer (ADMIN)
- `GET /api/products/{id}` - Détail
- `PUT /api/products/{id}` - Modifier (ADMIN)
- `DELETE /api/products/{id}` - Supprimer (ADMIN)

### Stocks
- `GET /api/stocks` - Lister mes stocks (PRODUCTEUR)
- `POST /api/stocks` - Créer stock
- `GET /api/stocks/{id}` - Détail
- `PUT /api/stocks/{id}` - Modifier
- `GET /api/stocks/search` - Recherche multicritères

### Stock Movements
- `POST /api/stocks/{id}/movements` - Ajouter mouvement
- `GET /api/stocks/{id}/movements` - Historique
- `GET /api/stocks/{id}/movements/export` - Export Excel

### Offers (Marché Virtuel)
- `GET /api/offers` - Lister offres
- `POST /api/offers` - Créer offre (PRODUCTEUR)
- `GET /api/offers/{id}` - Détail
- `PUT /api/offers/{id}` - Modifier (PRODUCTEUR)
- `DELETE /api/offers/{id}` - Supprimer (PRODUCTEUR)
- `GET /api/offers/search` - Recherche multicritères

### Reservations
- `POST /api/reservations` - Créer réservation (ACHETEUR)
- `GET /api/reservations` - Lister mes réservations
- `GET /api/reservations/{id}` - Détail
- `PUT /api/reservations/{id}/confirm` - Confirmer (PRODUCTEUR)
- `PUT /api/reservations/{id}/reject` - Rejeter (PRODUCTEUR)
- `DELETE /api/reservations/{id}` - Annuler

### Payments
- `POST /api/payments` - Créer paiement
- `GET /api/payments/{id}` - Détail
- `POST /api/payments/{id}/verify` - Vérifier paiement

### Ratings
- `POST /api/ratings` - Créer notation
- `GET /api/ratings/producer/{id}` - Notes d'un producteur

### Dashboard
- `GET /api/dashboard/stocks-by-region` - Stocks par région
- `GET /api/dashboard/stocks-by-filiere` - Stocks par filière
- `GET /api/dashboard/tension-zones` - Zones en tension
- `GET /api/dashboard/statistics` - Statistiques

### Admin
- `GET /api/admin/users` - Gestion utilisateurs
- `GET /api/admin/reports` - Rapports
- `GET /api/admin/audit-logs` - Logs d'audit

## 4. Sécurité

### Authentification
- JWT (JSON Web Tokens)
- Tokens d'accès (15 min)
- Refresh tokens (7 jours)
- Hashage des mots de passe (BCrypt)

### Autorisation
- RBAC (Role-Based Access Control)
- @PreAuthorize sur les endpoints sensibles
- Validation des permissions au niveau service

### Protection
- CSRF (Cross-Site Request Forgery)
- XSS (Cross-Site Scripting)
- SQL Injection (Prepared Statements)
- CORS configuré
- HTTPS obligatoire

### Audit
- Logs d'audit pour chaque action
- Traçabilité des modifications
- Enregistrement de l'IP utilisateur

## 5. Configuration Technique

### Stack
- **Backend** : Spring Boot 3.x
- **Base de données** : PostgreSQL 16
- **ORM** : JPA/Hibernate
- **Sécurité** : Spring Security + JWT
- **Documentation** : Swagger/OpenAPI 3.0
- **Logs** : Logback
- **Validation** : Jakarta Bean Validation
- **Mapping** : MapStruct

### Performance
- Temps de réponse : 100-200 ms (satisfaisant), 200ms-1s (acceptable)
- Utilisateurs simultanés : 100+
- Disponibilité : 99.9%
- Opérations/sec : 10 000+

### Infrastructure
- OS : Ubuntu 22.04 LTS
- RAM : 16 Go minimum
- Disque : 512 Go
- CPU : 8 GHz
- Docker pour déploiement
- Apache 2.4 (reverse proxy)
