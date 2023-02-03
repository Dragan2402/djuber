--ROLES
INSERT INTO role (id, name, deleted)
VALUES (1, 'ROLE_ADMIN', false);
INSERT INTO role (id, name, deleted)
VALUES (2, 'ROLE_CLIENT', false);
INSERT INTO role (id, name, deleted)
VALUES (3, 'ROLE_DRIVER', false);

--IDENTITIES
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (100000, 'asi@maildrop.cc', 0, '$2a$12$20EPQtCU8q4D.7/nRReWae5XaEJV0tOGRHats8mHWOAOzeT.bO7vW', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (200000, 'dmitar@maildrop.cc', 0, '$2a$12$UGtCSbOYCDC1WHKJl.u.2uqufpBSmFApXpHMM8s2HlyTxUnlsSpoC', false);

INSERT INTO identity_roles(identity_id, roles_id)
VALUES (100000, 1);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (200000, 1);

INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (300000, 'pero@maildrop.cc', 1, '$2a$12$Nd.axxCA/EUTgVpE6JU7yu1m0FbazoNKQSlh2p.b1dy.W1EfUzEwi', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (400000, 'jovo@maildrop.cc', 1, '$2a$12$pW0AC2EKW0sqMgERfj3ojuuD67cSKLwClksdhEOzZrIvdu8JWvqVW', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (300001, 'dejo@maildrop.cc', 1, '$2a$12$Nd.axxCA/EUTgVpE6JU7yu1m0FbazoNKQSlh2p.b1dy.W1EfUzEwi', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (400001, 'sejo@maildrop.cc', 1, '$2a$12$Nd.axxCA/EUTgVpE6JU7yu1m0FbazoNKQSlh2p.b1dy.W1EfUzEwi', false);

INSERT INTO identity_roles(identity_id, roles_id)
VALUES (300000, 2);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (400000, 2);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (300001, 2);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (400001, 2);

INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (500000, 'haso@maildrop.cc', 2, '$2a$12$B0M7RM24WZYet.lqGrIVWOGUKEaNCFgTAcT95qMIeua5/M0Tnzc6y', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (600000, 'mujo@maildrop.cc', 2, '$2a$12$bP24xyfnbEhigBAMI0cKOeJ.hng3f4kBYx/puFYeXzStIrSo5m/JK', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (500001, 'haso1@maildrop.cc', 2, '$2a$12$B0M7RM24WZYet.lqGrIVWOGUKEaNCFgTAcT95qMIeua5/M0Tnzc6y', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (600001, 'mujo1@maildrop.cc', 2, '$2a$12$bP24xyfnbEhigBAMI0cKOeJ.hng3f4kBYx/puFYeXzStIrSo5m/JK', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (500002, 'haso2@maildrop.cc', 2, '$2a$12$B0M7RM24WZYet.lqGrIVWOGUKEaNCFgTAcT95qMIeua5/M0Tnzc6y', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (600002, 'mujo2@maildrop.cc', 2, '$2a$12$bP24xyfnbEhigBAMI0cKOeJ.hng3f4kBYx/puFYeXzStIrSo5m/JK', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (500003, 'haso3@maildrop.cc', 2, '$2a$12$B0M7RM24WZYet.lqGrIVWOGUKEaNCFgTAcT95qMIeua5/M0Tnzc6y', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (600003, 'mujo3@maildrop.cc', 2, '$2a$12$bP24xyfnbEhigBAMI0cKOeJ.hng3f4kBYx/puFYeXzStIrSo5m/JK', false);

INSERT INTO identity_roles(identity_id, roles_id)
VALUES (500000, 3);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (600000, 3);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (500001, 3);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (600001, 3);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (500002, 3);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (600002, 3);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (500003, 3);
INSERT INTO identity_roles(identity_id, roles_id)
VALUES (600003, 3);

--ADMINS
INSERT INTO admin (id, identity_id, first_name, last_name, city, phone_number, deleted)
VALUES (100000, 100000, 'Andrej', 'Culjak', 'Novi Sad', '0677777778', false);

INSERT INTO admin (id, identity_id, first_name, last_name, city, phone_number, deleted)
VALUES (200000, 200000, 'Dimitrije', 'Petrov', 'Novi Sad', '067123456', false);

--CLIENTS
INSERT INTO client (id, identity_id, first_name, last_name, city, phone_number, verified, balance, signing_type,
                    blocked,
                    in_ride, note,
                    deleted)
VALUES (100000, 300000, 'Pero', 'Peric', 'Novi Sad', '0691777778', true, 10000, 0, false, false,
        'Client has been reporting drivers without valid reason.',
        false);

INSERT INTO client (id, identity_id, first_name, last_name, city, phone_number, verified, balance, signing_type,
                    blocked,
                    in_ride,
                    deleted)
VALUES (200000, 400000, 'Jovo', 'Jovic', 'Novi Sad', '067123423', true, 15000, 0, true, false,
        false);

INSERT INTO client (id, identity_id, first_name, last_name, city, phone_number, verified,balance, signing_type, blocked,
                    in_ride, note,
                    deleted)
VALUES (100001, 300001, 'Dejo', 'Dejic', 'Novi Sad', '0691777779', true, 10000, 0, false, false,
        'Client has been reporting drivers without valid reason.',
        false);

INSERT INTO client (id, identity_id, first_name, last_name, city, phone_number, verified,balance,  signing_type, blocked,
                    in_ride,
                    deleted)
VALUES (200001, 400001, 'Sejo', 'Sejic', 'Novi Sad', '067123424', true, 15000, 0, false, false,
        false);

--CARS
INSERT INTO car (id, car_type, license_plate, lat, lon, deleted)
VALUES (100000, 0, 'NS24423J', 45.23851423717639, 19.832768440246586, false);
INSERT INTO car (id, car_type, license_plate, lat, lon, deleted)
VALUES (200000, 0, 'NS99892J', 45.24138498327325, 19.846544265747074, false);
INSERT INTO car (id, car_type, license_plate, lat, lon, deleted)
VALUES (100001, 0, 'NS24423K', 45.260639, 19.832128, false);
INSERT INTO car (id, car_type, license_plate, lat, lon, deleted)
VALUES (200001, 0, 'NS99892K', 45.259850, 19.838841, false);
INSERT INTO car (id, car_type, license_plate, lat, lon, deleted)
VALUES (100002, 0, 'NS24423L', 45.253401, 19.843593, false);
INSERT INTO car (id, car_type, license_plate, lat, lon, deleted)
VALUES (200002, 0, 'NS99892L', 45.241263, 19.795964, false);
INSERT INTO car (id, car_type, license_plate, lat, lon, deleted)
VALUES (100003, 0, 'NS24423M', 45.240245, 19.827054, false);
INSERT INTO car (id, car_type, license_plate, lat, lon, deleted)
VALUES (200003, 0, 'NS99892M', 45.247923, 19.817227, false);


INSERT INTO CAR_ADDITIONAL_SERVICES (car_id, ADDITIONAL_SERVICES)
VALUES (100000, 'pets');
INSERT INTO CAR_ADDITIONAL_SERVICES (car_id, ADDITIONAL_SERVICES)
VALUES (100001, 'pets');
INSERT INTO CAR_ADDITIONAL_SERVICES (car_id, ADDITIONAL_SERVICES)
VALUES (100002, 'pets');
INSERT INTO CAR_ADDITIONAL_SERVICES (car_id, ADDITIONAL_SERVICES)
VALUES (100003, 'pets');

--DRIVERS
INSERT INTO driver (id,     identity_id, first_name, last_name, city,       phone_number, active, car_id, last_activation_time, blocked, in_ride, deleted)
VALUES             (100000, 500000,      'Haso',     'Hasic',   'Novi Sad', '0691777778', true,   100000, '2023-02-01 15:13',   false,   false,   false);
INSERT INTO driver (id,     identity_id, first_name, last_name, city,       phone_number, active, car_id, last_activation_time, blocked, in_ride, deleted)
VALUES             (200000, 600000,      'Mujo',     'Mujic',   'Novi Sad', '067123423',  false,  200000, '2023-02-01 15:15',   false,   false,   false);
INSERT INTO driver (id,     identity_id, first_name, last_name, city,       phone_number, active, car_id, last_activation_time, blocked, in_ride, deleted)
VALUES             (100001, 500001,      'Haso1',    'Hasic',   'Novi Sad', '0691777779', false,  100001, '2023-02-01 15:13',   false,   false,   false);
INSERT INTO driver (id,     identity_id, first_name, last_name, city,       phone_number, active, car_id, last_activation_time, blocked, in_ride, deleted)
VALUES             (200001, 600001,      'Mujo1',    'Mujic',   'Novi Sad', '067123424',  true,   200001, '2023-02-01 15:15',   false,   false,   false);
INSERT INTO driver (id,     identity_id, first_name, last_name, city,       phone_number, active, car_id, last_activation_time, blocked, in_ride, deleted)
VALUES             (100002, 500002,      'Haso2',    'Hasic',   'Novi Sad', '0691777780', true,   100002, '2023-02-01 15:13',   false,   false,   false);
INSERT INTO driver (id,     identity_id, first_name, last_name, city,       phone_number, active, car_id, last_activation_time, blocked, in_ride, deleted)
VALUES             (200002, 600002,      'Mujo2',    'Mujic',   'Novi Sad', '067123425',  true,   200002, '2023-02-01 15:15',   false,   false,   false);
INSERT INTO driver (id,     identity_id, first_name, last_name, city,       phone_number, active, car_id, last_activation_time, blocked, in_ride, deleted)
VALUES             (100003, 500003,      'Haso3',    'Hasic',   'Novi Sad', '0691777781', true,   100003, '2023-02-01 15:13',   false,   false,   false);
INSERT INTO driver (id,     identity_id, first_name, last_name, city,       phone_number, active, car_id, last_activation_time, blocked, in_ride, deleted)
VALUES             (200003, 600003,      'Mujo3',    'Mujic',   'Novi Sad', '067123426',  true,   200003, '2023-02-01 15:15',   false,   false,   false);

-- --DRIVER LOGS
-- INSERT INTO driver_active_log(id, driver_id, log_start, log_end) VALUES (100000, 100000, '2023-01-24 14:15', '2023-01-24 16:15' );
-- INSERT INTO driver_active_log(id, driver_id, log_start, log_end) VALUES (200000, 100000, '2023-01-24 18:15', '2023-01-25 03:50' );
-- INSERT INTO driver_active_log(id, driver_id, log_start, log_end) VALUES (300000, 100000, '2023-01-25 10:15', '2023-01-25 11:15' );

--ROUTES
INSERT INTO route (id, deleted)
VALUES (100000, false);

INSERT INTO route (id, deleted)
VALUES (200000, false);

INSERT INTO route (id, deleted)
VALUES (300000, false);

INSERT INTO route_stop_names  (route_id, stop_names)VALUES (100000, 'Brcko');
INSERT INTO route_stop_names  (route_id, stop_names)VALUES (100000, 'Pariz');
INSERT INTO route_stop_names  (route_id, stop_names)VALUES (200000, 'London');
INSERT INTO route_stop_names  (route_id, stop_names)VALUES (200000, 'Lisabon');
INSERT INTO route_stop_names  (route_id, stop_names)VALUES (300000, 'Keln');
INSERT INTO route_stop_names  (route_id, stop_names)VALUES (300000, 'Munih');

--COORDINATES
INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (100000, 0, 'Promenada', 45.24473173439358, 19.84146283175646, 100000);
INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (200000, 1, NULL, 45.2480590388405, 19.839219531462838, 100000);
INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (300000, 2, 'Balans Palacinkarnica', 45.25258638691436, 19.83754453391027, 100000);


INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (400000, 0, 'Pekara Evropa', 45.240182962558684, 19.825826428862268, 200000);
INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (500000, 1, NULL, 45.2480590388405, 19.839219531462838, 200000);
INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (600000, 2, 'Medicinski Fakultet Novi Sad', 45.25328151383017, 19.824271073957725, 300000);

INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (700000, 0, 'Pekara Evropa', 45.240182962558684, 19.825826428862268, 300000);
INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (800000, 1, NULL, 45.2480590388405, 19.839219531462838, 300000);
INSERT INTO coordinate(id, index, location_name, lat, lon, route_id)
VALUES (900000, 2, 'Medicinski Fakultet Novi Sad', 45.25328151383017, 19.824271073957725, 300000);

--RIDES
INSERT INTO ride (id, driver_id, ride_type, car_type, start, finish, route_id, price, ride_status, deleted, cancelling_note)
VALUES (100000, 100000, 0, 0, '2022-10-23 00:00', NULL, 100000, 599, 0, false, NULL );

INSERT INTO ride (id, driver_id, ride_type, car_type, start, finish, route_id, price, ride_status, deleted, cancelling_note)
VALUES (200000, 100000, 0, 0, '2023-01-25 12:00', '2023-01-25 14:15', 300000, 709, 2, false, NULL );

--REQUESTED_SERVICES
INSERT INTO RIDE_REQUESTED_SERVICES (ride_id, REQUESTED_SERVICES)
VALUES (100000, 'pets');

INSERT INTO clients_rides (client_id, ride_id)
VALUES (100000, 100000);

INSERT INTO clients_rides (client_id, ride_id)
VALUES (100000, 200000);

--RESERVATIONS
INSERT INTO reservation (id, ride_type, start, route_id, price, deleted)
VALUES (100000, 1, '2023-01-26 15:23', 200000, 40, false);

INSERT INTO clients_reservations (RESERVATION_ID, CLIENT_ID)
VALUES (100000, 200000);

--REVIEWS
INSERT INTO review (id, driver_id, client_id, ride_id, car_rating,driver_rating, comment, deleted)
VALUES (100000, 100000, 100000, 100000, 4.8, 2.2, 'Fin i kulturan vozac', false);

-- INSERT INTO review (id, driver_id, client_id, ride_id, car_rating,driver_rating, comment, deleted)
-- VALUES (200000, 100000, 100000, 200000, 4.8, 2.2, 'Fin i kulturan vozac', false );

--CHATS
INSERT INTO chat (id, subject_identity_id, deleted)
VALUES (100000, 400000, false);
INSERT INTO chat (id, subject_identity_id, deleted)
VALUES (200000, 500000, false);
INSERT INTO chat (id, subject_identity_id, deleted)
VALUES (300000, 600000, false);

--MESSAGES
INSERT INTO message (id, chat_id, sender_name, content, time, from_admin, deleted)
VALUES (100000, 100000, 'Jovo Jovic', 'Pozdrav admine imam problem', CURRENT_TIMESTAMP, false, false);
INSERT INTO message (id, chat_id, sender_name, content, time, from_admin, deleted)
VALUES (300000, 200000, 'Haso Hasic', 'Pozdrav admine imam dvjesta problem', CURRENT_TIMESTAMP, false, false);
INSERT INTO message (id, chat_id, sender_name, content, time, from_admin, deleted)
VALUES (400000, 300000, 'Mujo Mujic', 'Pozdrav admine imam trista problem', CURRENT_TIMESTAMP, false, false);
INSERT INTO message (id, chat_id, sender_name, content, time, from_admin, deleted)
VALUES (200000, 100000, 'Andrej Culjak', 'Kazite gospodine', CURRENT_TIMESTAMP, true, false);
