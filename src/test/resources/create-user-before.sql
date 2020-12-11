delete from user_role;
delete from usr;

insert into usr(id, username, password, active) values
(1, 'admin', '$2a$08$sf7K4a/DQbAD6G1.3PKWMudwgytD4iNPayXz3N2DBvshwGp1Qzu0e', true),
(2, 'trial', '$2a$08$sf7K4a/DQbAD6G1.3PKWMudwgytD4iNPayXz3N2DBvshwGp1Qzu0e', true);

insert into user_role(user_id, roles) values
(1, 'ADMIN'), (1, 'USER'),
(2, 'USER');