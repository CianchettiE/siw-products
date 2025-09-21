-- Utenti e Credenziali
-- Password per entrambi: password
INSERT INTO users (id, name, surname) VALUES (1, 'Mario', 'Rossi');
INSERT INTO credentials (id, username, password, role, user_id) VALUES (1, 'mario', '$2a$10$S/OLI6a8I9e.fl9T544BOuENRB1iM8VGS3v2aJb3JtQtra14sR.5O', 'DEFAULT', 1);

INSERT INTO users (id, name, surname) VALUES (2, 'Admin', 'Admin');
INSERT INTO credentials (id, username, password, role, user_id) VALUES (2, 'admin', '$2a$10$S/OLI6a8I9e.fl9T544BOuENRB1iM8VGS3v2aJb3JtQtra14sR.5O', 'ADMIN', 2);

-- Prodotti
INSERT INTO product (id, name, price, description, type) VALUES (101, 'Il Signore degli Anelli', 25.50, 'Un classico della letteratura fantasy.', 'Libri');
INSERT INTO product (id, name, price, description, type) VALUES (102, 'Tosaerba Elettrico', 150.00, 'Potente e silenzioso, ideale per giardini di medie dimensioni.', 'Giardinaggio');
INSERT INTO product (id, name, price, description, type) VALUES (103, 'Lo Hobbit', 15.00, 'Il prequel de Il Signore degli Anelli.', 'Libri');
INSERT INTO product (id, name, price, description, type) VALUES (104, 'Set di attrezzi da giardino', 45.99, 'Include tutto il necessario per la cura del tuo giardino.', 'Giardinaggio');
