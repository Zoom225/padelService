# ARCHITECTURE.md — Dossier d'architecture
## Vue générale
Application **REST API** Spring Boot 3.5 / Java 21 avec frontend Angular séparé.
```
[Angular Frontend :4200]  ←──HTTP/JSON──→  [Spring Boot API :8080/api]  ←──JPA/Hibernate──→  [PostgreSQL :5432]
```
---
## Architecture Backend (Spring Boot)
### Couches
```
controller/      ← REST Controllers (@RestController) - HTTP In/Out
service/         ← Logique métier (@Service) - Règles business
repository/      ← Accès données (JpaRepository) - Requêtes DB
model/entity/    ← Entités JPA (@Entity) - Mapping tables
model/enums/     ← Enumerations
dto/             ← Data Transfer Objects (à implémenter)
config/          ← Configuration Spring (Swagger, CORS, Scheduler)
exception/       ← Gestion globale des erreurs
```
### Diagramme ER (entités principales)
```
Site 1─────────* Terrain
Site 1─────────* HoraireSite
Site 1─────────* FermetureJour
Site 1─────────* User (membres site)
User 1─────────* Reservation (organizer)
Terrain 1──────* Reservation
Reservation 1──1 Match
Match 1────────* JoueurMatch *────1 User
Match 1────────* Paiement *────── 1 User
Match 1────────* Penalite ────── *User
User 1─────────* Administrator
Administrator *─────────────────1 Site (optionnel)
```
### Règles métier clés
| Type utilisateur | Prefix | Sites autorisés | Délai réservation |
|-----------------|--------|-----------------|-------------------|
| GLOBAL          | G      | Tous            | 21 jours max      |
| SITE            | S      | Son site seul   | 14 jours max      |
| LIBRE           | L      | Tous            | 5 jours max       |
---
## Librairies et frameworks
| Technologie           | Version  | Rôle                         |
|----------------------|----------|------------------------------|
| Spring Boot           | 3.5.0    | Framework backend             |
| Java                  | 21       | Langage                       |
| PostgreSQL            | 16       | Base de données               |
| Flyway                | intégré  | Migration DB                  |
| Hibernate/JPA         | intégré  | ORM                           |
| Lombok                | intégré  | Réduction boilerplate         |
| SpringDoc OpenAPI     | 2.0.2    | Documentation Swagger         |
| JaCoCo                | 0.8.10   | Couverture de tests           |
| JUnit 5 + Mockito     | intégré  | Tests unitaires               |
| TestContainers        | 1.19.0   | Tests intégration             |
| Docker                | latest   | Conteneurisation DB           |
---
## URL Swagger (dev local)
http://localhost:8080/api/swagger-ui.html
---
## Patterns utilisés
- **Injection de dépendances** : @RequiredArgsConstructor + constructeur
- **Repository Pattern** : JpaRepository par entité
- **Service Layer** : logique métier isolée
- **Exception globale** : @RestControllerAdvice
- **Scheduler** : vérification quotidienne pénalités / paiements
- **Builder Pattern** : Lombok @Builder sur toutes les entités
