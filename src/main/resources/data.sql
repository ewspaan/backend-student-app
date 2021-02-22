-- INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);
-- INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);
-- INSERT INTO users (username, password, enabled) VALUES ('peter', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);

-- INSERT INTO app_users (first_name, last_name, email, username, date_of_birth, password ) VALUES ('Erwin','Spaan','ewspaan@gmail.com','ews', '11-11-1977','$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica');
-- INSERT INTO app_users (first_name, last_name, email, username, date_of_birth, password ) VALUES ('Ralph','Spaan','rjspaan@gmail.com','rjs', '04-10-1978','$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica');

INSERT INTO role(name) VALUES('ROLE_USER');
INSERT INTO role(name) VALUES('ROLE_ADMIN');
INSERT INTO role(name) VALUES('ROLE_MODERATOR');



-- Insert Into clients (first_name, last_name, email, date_of_birth, password, enabled) VALUES ('Erwin','Spaan','ewspaan@gmail.com','11-11-1977','password', TRUE);
-- Insert Into clients (first_name, last_name, email, date_of_birth, password, enabled) VALUES ('Ralph','Spaan','rjspaan@gmail.com','04-10-1978','password', TRUE);
-- Insert Into clients (first_name, last_name, email, date_of_birth, password, enabled) VALUES ('Erwin','Spaan','ewspaan@gmail.com','06-04-1981','password', TRUE);