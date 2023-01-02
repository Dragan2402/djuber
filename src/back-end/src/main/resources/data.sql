--ROLES
INSERT INTO role (id, name, deleted) VALUES (1, 'ROLE_ADMIN', false);
INSERT INTO role (id, name, deleted) VALUES (2, 'ROLE_CLIENT', false);
INSERT INTO role (id, name, deleted) VALUES (3, 'ROLE_DRIVER', false);

--IDENTITIES
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (100000, 'asi@maildrop.cc', 0, '$2a$12$20EPQtCU8q4D.7/nRReWae5XaEJV0tOGRHats8mHWOAOzeT.bO7vW', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (200000, 'dmitar@maildrop.cc', 0, '$2a$12$UGtCSbOYCDC1WHKJl.u.2uqufpBSmFApXpHMM8s2HlyTxUnlsSpoC', false);

INSERT INTO identity_roles(identity_id, roles_id) VALUES (100000,1);
INSERT INTO identity_roles(identity_id, roles_id) VALUES (200000,1);

INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (300000, 'pero@maildrop.cc', 1, '$2a$12$Nd.axxCA/EUTgVpE6JU7yu1m0FbazoNKQSlh2p.b1dy.W1EfUzEwi', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (400000, 'jovo@maildrop.cc', 1, '$2a$12$pW0AC2EKW0sqMgERfj3ojuuD67cSKLwClksdhEOzZrIvdu8JWvqVW', false);

INSERT INTO identity_roles(identity_id, roles_id) VALUES (300000,2);
INSERT INTO identity_roles(identity_id, roles_id) VALUES (400000,2);

INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (500000, 'haso@maildrop.cc', 2, '$2a$12$B0M7RM24WZYet.lqGrIVWOGUKEaNCFgTAcT95qMIeua5/M0Tnzc6y', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (600000, 'mujo@maildrop.cc', 2, '$2a$12$bP24xyfnbEhigBAMI0cKOeJ.hng3f4kBYx/puFYeXzStIrSo5m/JK', false);

INSERT INTO identity_roles(identity_id, roles_id) VALUES (500000,3);
INSERT INTO identity_roles(identity_id, roles_id) VALUES (600000,3);

--ADMINS
INSERT INTO admin (id, identity_id, first_name, last_name, city, phone_number, picture, deleted)
VALUES (100000, 100000, 'Andrej', 'Culjak', 'Novi Sad', '0677777778', 'slikaTODO', false);

INSERT INTO admin (id, identity_id, first_name, last_name, city, phone_number, picture, deleted)
VALUES (200000, 200000, 'Dimitrije', 'Petrov', 'Novi Sad', '067123456', 'slikaTODO', false);

--CLIENTS
INSERT INTO client (id, identity_id, first_name, last_name, city, phone_number, picture, verified, blocked, in_ride,
                    deleted)
VALUES (100000, 300000, 'Pero', 'Peric', 'Novi Sad', '0691777778', 'slikaTODO', true, false, false,
        false);

INSERT INTO client (id, identity_id, first_name, last_name, city, phone_number, picture, verified, blocked, in_ride,
                    deleted)
VALUES (200000, 400000, 'Jovo', 'Jovic', 'Novi Sad', '067123423', 'slikaTODO', true, false, false,
        false);

--CARS
INSERT INTO car (id, car_type, x, y, deleted)
VALUES (100000, 0, 45.24533662754101, 19.8430497771721, false);

INSERT INTO car (id, car_type, x, y, deleted)
VALUES (200000, 0, 45.24533662754101, 19.8430497771721, false);

INSERT INTO CAR_ADDITIONAL_SERVICES (car_id, ADDITIONAL_SERVICES)
VALUES (100000, 'Ljubimci do 15kg');

--DRIVERS
INSERT INTO driver (id, identity_id, first_name, last_name, city, phone_number, picture, active, car_id,
                    duration_active, blocked, in_ride,
                    deleted)
VALUES (100000, 500000, 'Haso', 'Hasic', 'Novi Sad', '0691777778', 'slikaTODO', true, 100000, 0, false,
        false, false);

INSERT INTO driver (id, identity_id, first_name, last_name, city, phone_number, picture, active, car_id,
                    duration_active, blocked, in_ride,
                    deleted)
VALUES (200000, 600000, 'Jovo', 'Jovic', 'Novi Sad', '067123423', 'slikaTODO', true, 200000, 0, false,
        false, false);

--ROUTES
INSERT INTO route (id, deleted)
VALUES (100000, false);

INSERT INTO route (id, deleted)
VALUES (200000, false);

--COORDINATES
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (100000, 0, 'Promenada', 45.24473173439358, 19.84146283175646, 100000, true);
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (200000, 1, NULL, 45.2480590388405, 19.839219531462838, 100000, false);
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (300000, 2, 'Balans Palacinkarnica', 45.25258638691436, 19.83754453391027, 100000, true);

INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (400000, 0, 'Pekara Evropa', 45.240182962558684, 19.825826428862268, 200000, true);
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (500000, 1, NULL, 45.2480590388405, 19.839219531462838, 200000, false);
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (600000, 2, 'Medicinski Fakultet Novi Sad', 45.25328151383017, 19.824271073957725, 200000, true);

--RIDES
INSERT INTO ride (id, driver_id, ride_type, start, finish, route_id, price, ride_status, deleted)
VALUES (100000, 100000, 0, '2022-10-23 00:00', NULL, 100000, 110, 0, false);

INSERT INTO clients_rides (client_id, ride_id)
VALUES (100000, 100000);

--RESERVATIONS
INSERT INTO reservation (id, driver_id, ride_type, start, route_id, price, deleted)
VALUES (100000, 200000, 1, '2022-12-22 13:00', 200000, 40, false);

INSERT INTO clients_reservations (RESERVATION_ID, CLIENT_ID)
VALUES (100000, 200000);

--REVIEWS
INSERT INTO review (id, driver_id, client_id, ride_id, rating, comment, deleted)
VALUES (100000, 100000, 100000, 100000, 4.8, 'Fin i kulturan vozac', false);