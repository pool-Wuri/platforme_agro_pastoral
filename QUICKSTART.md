# Guide de Démarrage Rapide - Backend SUIVI_AGRO_PASTORAL

## 1. Installation Rapide

### Étape 1 : Prérequis
Assurez-vous que vous avez installé :
- Java 17 ou supérieur
- PostgreSQL 16
- Maven 3.6+

### Étape 2 : Configuration de la Base de Données

```bash
# Accédez à PostgreSQL
psql -U postgres

# Créez la base de données
CREATE DATABASE agropastoral;
```

### Étape 3 : Configuration de l'Application

Modifiez `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/agropastoral
spring.datasource.username=postgres
spring.datasource.password=<votre_mot_de_passe>
spring.jpa.hibernate.ddl-auto=create-drop  # Pour la première exécution

jwt.secret=your-super-secret-key-that-is-long-enough-to-be-secure
jwt.expiration=86400000
```

### Étape 4 : Lancer l'Application

```bash
mvn clean install
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`

## 2. Premiers Pas

### Accéder à la Documentation API

Ouvrez votre navigateur et allez à :
```
http://localhost:8080/swagger-ui.html
```

### Créer un Compte Admin

Utilisez Swagger pour faire une requête POST à `/api/auth/register` :

```json
{
  "email": "admin@example.com",
  "password": "SecurePassword123!",
  "firstName": "Admin",
  "lastName": "User",
  "phoneNumber": "+226XXXXXXXX",
  "role": "ROLE_ADMIN"
}
```

### Se Connecter

Utilisez `/api/auth/login` avec vos identifiants :

```json
{
  "email": "admin@example.com",
  "password": "SecurePassword123!"
}
```

Vous recevrez un token JWT à utiliser pour les requêtes authentifiées.

## 3. Utilisation de l'API

### Ajouter le Token JWT dans les Requêtes

Dans Swagger, cliquez sur le bouton "Authorize" en haut à droite et entrez :

```
Bearer <votre_token_jwt>
```

### Créer un Produit (Admin)

POST `/api/products` :

```json
{
  "name": "Riz",
  "description": "Riz blanc de qualité",
  "category": "Céréales",
  "unit": "kg"
}
```

### Créer un Stock (Producteur)

POST `/api/stocks` :

```json
{
  "productId": "<uuid_du_produit>",
  "quantity": 1000,
  "region": "Kadiogo"
}
```

### Créer une Offre (Producteur)

POST `/api/offers` :

```json
{
  "productId": "<uuid_du_produit>",
  "quantity": 500,
  "pricePerUnit": 250.00,
  "region": "Kadiogo",
  "expiryDate": "2026-03-24T23:59:59"
}
```

### Créer une Réservation (Acheteur)

POST `/api/reservations` :

```json
{
  "offerId": "<uuid_de_l_offre>",
  "quantity": 100,
  "reservedUntil": "2026-02-28T23:59:59"
}
```

## 4. Rôles et Permissions

| Rôle | Cas d'Usage |
|------|------------|
| ROLE_ADMIN | Gestion des produits, modération, rapports |
| ROLE_PRODUCTEUR | Gestion des stocks et offres |
| ROLE_ACHETEUR | Consultation et réservation d'offres |
| ROLE_AUTORITE | Consultation des statistiques |

## 5. Troubleshooting

### Erreur de Connexion à la Base de Données

Vérifiez que PostgreSQL est en cours d'exécution :

```bash
# Linux/Mac
pg_isready

# Windows
psql -U postgres -c "SELECT 1"
```

### Erreur de Port Déjà Utilisé

Changez le port dans `application.properties` :

```properties
server.port=8081
```

### Erreur de Token JWT Invalide

Assurez-vous que le token n'a pas expiré et qu'il est correctement formaté avec le préfixe "Bearer ".

## 6. Ressources Utiles

- **Documentation Swagger** : `http://localhost:8080/swagger-ui.html`
- **Santé de l'Application** : `http://localhost:8080/actuator/health`
- **Métriques** : `http://localhost:8080/actuator/metrics`

## 7. Prochaines Étapes

1. Déployer sur un serveur de production
2. Configurer HTTPS
3. Mettre en place la sauvegarde automatique
4. Configurer le monitoring et les alertes
5. Intégrer avec le frontend Angular

## Support

Pour toute assistance, consultez le fichier README.md complet ou contactez l'équipe de développement.
