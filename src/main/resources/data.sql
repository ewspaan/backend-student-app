-- INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);
-- INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);
-- INSERT INTO users (username, password, enabled) VALUES ('peter', '$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica', TRUE);

-- INSERT INTO app_users (first_name, last_name, email, username, date_of_birth, password ) VALUES ('Erwin','Spaan','ewspaan@gmail.com','ews', '11-11-1977','$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica');
-- INSERT INTO app_users (first_name, last_name, email, username, date_of_birth, password ) VALUES ('Ralph','Spaan','rjspaan@gmail.com','rjs', '04-10-1978','$2a$10$wPHxwfsfTnOJAdgYcerBt.utdAvC24B/DWfuXfzKBSDHO0etB1ica');

INSERT INTO role(name) VALUES('ROLE_USER');
INSERT INTO role(name) VALUES('ROLE_ADMIN');
INSERT INTO role(name) VALUES('ROLE_MODERATOR');

INSERT INTO houses (id, account_number, house_name)VALUES (11, '12345678', 'MacBla' );

INSERT INTO app_users (id, date_of_birth, email, first_name, last_name, password, username, house_id )
VALUES (67,'01-02-1903','MacBla@gmail.com','Bla','MacBla','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG', 'macbla', (select id from houses where house_name = 'MacBla'));
INSERT INTO app_users (id, date_of_birth, email, first_name, last_name, password, username, house_id )
VALUES (78,'01-05-1949','MacBlaBla@gmail.com','Blaat','MacBla','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG', 'macblaat', (select id from houses where house_name = 'MacBla'));

INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_MODERATOR'),(SELECT id FROM app_users where username = 'macbla'));
INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_USER'),(SELECT id FROM app_users where username = 'macblaat'));

INSERT INTO accounts (house_id , account_number, elektra_utility, gas_utility, internet_utility,
                      water_utility, total_amount_utilities)  VALUES ((SELECT house_id FROM app_users where username = 'macbla') , '12345678' , 12.50 , 12.80, 13.40 , 14.90, 0);


