
INSERT INTO role(name) VALUES('ROLE_USER');
INSERT INTO role(name) VALUES('ROLE_ADMIN');
INSERT INTO role(name) VALUES('ROLE_MODERATOR');

INSERT INTO houses ( house_name)VALUES ( 'MacBla' );


INSERT INTO app_users ( date_of_birth, email, first_name, last_name, password, username, house_id )
VALUES ('01-02-1903','MacBla@gmail.com','Bla','MacBla','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG', 'macbla', (select id from houses where house_name = 'MacBla'));

INSERT INTO app_users ( date_of_birth, email, first_name, last_name, password, username, house_id )
VALUES ('01-05-1949','MacBlaBla@gmail.com','Blaat','MacBla','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG', 'macblaat', (select id from houses where house_name = 'MacBla'));

INSERT INTO app_users ( date_of_birth, email, first_name, last_name, password, username, house_id )
VALUES ('01-05-1949','MacBlaBla1@gmail.com','Blaat1','MacBla','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG', 'macblaat1', (select id from houses where house_name = 'MacBla'));

INSERT INTO app_users ( date_of_birth, email, first_name, last_name, password, username, house_id )
VALUES ('01-05-1949','MacBlaBla2@gmail.com','Blaat2','MacBla','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG', 'macblaat2', (select id from houses where house_name = 'MacBla'));

INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_MODERATOR'),(SELECT id FROM app_users where username = 'macbla'));
INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_USER'),(SELECT id FROM app_users where username = 'macblaat'));
INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_USER'),(SELECT id FROM app_users where username = 'macblaat1'));
INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_USER'),(SELECT id FROM app_users where username = 'macblaat2'));

INSERT INTO accounts (house_id , account_number, elektra_utility, gas_utility, internet_utility,
                      water_utility, total_amount_utilities)  VALUES ((SELECT house_id FROM app_users where username = 'macbla') , '12345678' , 12.50 , 12.80, 17.20 , 17.50, 60);


insert into bills ( month, payed, total_amount, total_declarations, total_utilities, year, house_id) values ( '369', false, 13.22, 81.66, 14.62, 2010, (SELECT house_id FROM app_users where username = 'macbla'));
insert into bills ( month, payed, total_amount, total_declarations, total_utilities, year, house_id) values ( '000', false, 78.23, 80.69, 89.12, 2010, (SELECT house_id FROM app_users where username = 'macbla'));
insert into bills ( month, payed, total_amount, total_declarations, total_utilities, year, house_id) values ( '910', true, 28.74, 16.01, 99.74, 1999, (SELECT house_id FROM app_users where username = 'macbla'));
insert into bills ( month, payed, total_amount, total_declarations, total_utilities, year, house_id) values ( '69', false, 82.13, 36.43, 12.95, 1999, (SELECT house_id FROM app_users where username = 'macbla'));
insert into bills ( month, payed, total_amount, total_declarations, total_utilities, year, house_id) values ( '51', true, 57.76, 91.18, 10.83, 2012, (SELECT house_id FROM app_users where username = 'macbla'));
