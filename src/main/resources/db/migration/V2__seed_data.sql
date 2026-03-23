-- V2__seed_data.sql
-- Donnees de test pour Padel Service
-- Sites
INSERT INTO sites (name, description, address, number_of_courts, opening_time, closing_time, price_per_match) VALUES
('Padel Brussels Centre', 'Site principal au centre de Bruxelles', 'Rue de la Loi 1, 1000 Bruxelles', 4, '08:00', '22:00', 60.00),
('Padel Liege Arena', 'Site de Liege avec 3 terrains couverts', 'Boulevard de la Sauveniere 10, 4000 Liege', 3, '09:00', '21:00', 60.00),
('Padel Namur Club', 'Club de padel au coeur de Namur', 'Avenue de la Gare 5, 5000 Namur', 2, '10:00', '20:00', 60.00)
ON CONFLICT (name) DO NOTHING;
-- Terrains site 1 (Brussels)
INSERT INTO terrains (name, site_id, court_number) VALUES
('Terrain A', 1, 1), ('Terrain B', 1, 2),
('Terrain C', 1, 3), ('Terrain D', 1, 4)
ON CONFLICT (site_id, court_number) DO NOTHING;
-- Terrains site 2 (Liege)
INSERT INTO terrains (name, site_id, court_number) VALUES
('Court 1', 2, 1), ('Court 2', 2, 2), ('Court 3', 2, 3)
ON CONFLICT (site_id, court_number) DO NOTHING;
-- Terrains site 3 (Namur)
INSERT INTO terrains (name, site_id, court_number) VALUES
('Piste 1', 3, 1), ('Piste 2', 3, 2)
ON CONFLICT (site_id, court_number) DO NOTHING;
-- Horaires 2026 pour chaque site
INSERT INTO horaires_site (site_id, annee, heure_ouverture, heure_fermeture, dure_match, pause_entre_maches) VALUES
(1, 2026, '08:00', '22:00', 90, 15),
(2, 2026, '09:00', '21:00', 90, 15),
(3, 2026, '10:00', '20:00', 90, 15)
ON CONFLICT (site_id, annee) DO NOTHING;
-- Utilisateurs : Membres Globaux
INSERT INTO users (matricule, first_name, last_name, email, phone, user_type) VALUES
('G0001', 'Alice', 'Dupont', 'alice.dupont@email.be', '+32 470 11 22 33', 'GLOBAL'),
('G0002', 'Bob', 'Martin', 'bob.martin@email.be', '+32 471 44 55 66', 'GLOBAL'),
('G0003', 'Claire', 'Laurent', 'claire.laurent@email.be', '+32 472 77 88 99', 'GLOBAL')
ON CONFLICT (matricule) DO NOTHING;
-- Utilisateurs : Membres Site (Brussels = site 1)
INSERT INTO users (matricule, first_name, last_name, email, phone, user_type, site_id) VALUES
('S00001', 'David', 'Renard', 'david.renard@email.be', '+32 473 00 11 22', 'SITE', 1),
('S00002', 'Emma', 'Simon', 'emma.simon@email.be', '+32 474 33 44 55', 'SITE', 1),
('S00003', 'Felix', 'Petit', 'felix.petit@email.be', '+32 475 66 77 88', 'SITE', 2),
('S00004', 'Grace', 'Bernard', 'grace.bernard@email.be', '+32 476 99 00 11', 'SITE', 3)
ON CONFLICT (matricule) DO NOTHING;
-- Utilisateurs : Membres Libres
INSERT INTO users (matricule, first_name, last_name, email, phone, user_type) VALUES
('L00001', 'Hugo', 'Moreau', 'hugo.moreau@email.be', '+32 477 22 33 44', 'LIBRE'),
('L00002', 'Iris', 'Leroy', 'iris.leroy@email.be', '+32 478 55 66 77', 'LIBRE')
ON CONFLICT (matricule) DO NOTHING;
-- Admin Global (user G0001)
INSERT INTO administrators (user_id, admin_type, password)
SELECT id, 'GLOBAL', '$2a$10$dummyhashedpassword123'
FROM users WHERE matricule = 'G0001'
ON CONFLICT DO NOTHING;
-- Admin Site Brussels (user S00001)
INSERT INTO administrators (user_id, admin_type, site_id, password)
SELECT u.id, 'SITE', 1, '$2a$10$dummyhashedpassword456'
FROM users u WHERE u.matricule = 'S00001'
ON CONFLICT DO NOTHING;
-- Jours de fermeture globaux (jours feries 2026)
INSERT INTO fermeture_jours (date_fermeture, motif, est_global) VALUES
('2026-01-01', 'Nouvel An', TRUE),
('2026-04-05', 'Paques', TRUE),
('2026-05-01', 'Fete du Travail', TRUE),
('2026-07-21', 'Fete Nationale', TRUE),
('2026-11-01', 'Toussaint', TRUE),
('2026-12-25', 'Noel', TRUE)
ON CONFLICT DO NOTHING;
