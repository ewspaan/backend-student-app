-- INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);
-- INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);
-- INSERT INTO users (username, password, enabled) VALUES ('peter', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);

-- INSERT INTO app_users (first_name, last_name, email, username, date_of_birth, password ) VALUES ('Erwin','Spaan','ewspaan@gmail.com','ews', '11-11-1977','$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica');
-- INSERT INTO app_users (first_name, last_name, email, username, date_of_birth, password ) VALUES ('Ralph','Spaan','rjspaan@gmail.com','rjs', '04-10-1978','$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica');

INSERT INTO role(name) VALUES('ROLE_USER');
INSERT INTO role(name) VALUES('ROLE_ADMIN');
INSERT INTO role(name) VALUES('ROLE_MODERATOR');

INSERT INTO houses (id, account_number, house_name)VALUES (6 , '12345678', 'spaan' );

INSERT INTO app_users (id, date_of_birth, email, first_name, last_name, password, username, house_id )
VALUES (67,'20-05-1944','hspaan@gmail.com','Henk','Spaan','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG', 'hspaan', (select id from houses where house_name = 'spaan'));
INSERT INTO app_users (id, date_of_birth, email, first_name, last_name, password, username, house_id )
VALUES (78,'01-05-1949','vspaan@gmail.com','Vroni','Spaan','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG', 'vspaan', (select id from houses where house_name = 'spaan'));

INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_MODERATOR'),(SELECT id FROM app_users where username = 'vspaan'));
INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_USER'),(SELECT id FROM app_users where username = 'hspaan'));


