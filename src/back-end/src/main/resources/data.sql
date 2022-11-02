--IDENTITIES
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (1, 'asi@gmaildrop.cc', 1, 'asi123', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (2, 'dmitar@gmaildrop.cc', 1, 'dmitar123', false);

INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (3, 'pero@gmaildrop.cc', 2, 'pero123', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (4, 'jovo@gmaildrop.cc', 2, 'jovo123', false);

INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (5, 'haso@gmaildrop.cc', 3, 'haso123', false);
INSERT INTO identity (id, email, user_type, password, deleted)
VALUES (6, 'mujo@gmaildrop.cc', 3, 'mujo123', false);

--ADMINS
INSERT INTO admin (id, identity_id, first_name, last_name, city, phone_number, picture, deleted)
VALUES (1, 1, 'Andrej', 'Culjak', 'Novi Sad', '0677777778', 'slikaTODO', false);

INSERT INTO admin (id, identity_id, first_name, last_name, city, phone_number, picture, deleted)
VALUES (2, 2, 'Dimitrije', 'Petrov', 'Novi Sad', '067123456', 'slikaTODO', false);

--CLIENTS
INSERT INTO client (id, identity_id, first_name, last_name, city, phone_number, picture, verified, blocked, in_ride,
                    deleted)
VALUES (1, 3, 'Pero', 'Peric', 'Novi Sad', '0691777778', 'slikaTODO', true, false, false,
        false);

INSERT INTO client (id, identity_id, first_name, last_name, city, phone_number, picture, verified, blocked, in_ride,
                    deleted)
VALUES (2, 4, 'Jovo', 'Jovic', 'Novi Sad', '067123423', 'slikaTODO', true, false, false,
        false);

--CARS
INSERT INTO car (id, car_type, x, y, deleted)
VALUES (1, 0, 45.24533662754101, 19.8430497771721, false);

INSERT INTO car (id, car_type, x, y, deleted)
VALUES (2, 0, 45.24533662754101, 19.8430497771721, false);

INSERT INTO CAR_ADDITIONAL_SERVICES (car_id, ADDITIONAL_SERVICES)
VALUES (1, 'Ljubimci do 15kg');

--DRIVERS
INSERT INTO driver (id, identity_id, first_name, last_name, city, phone_number, picture, active, car_id,
                    duration_active, blocked, in_ride,
                    deleted)
VALUES (1, 5, 'Haso', 'Hasic', 'Novi Sad', '0691777778', 'slikaTODO', true, 1, 0, false,
        false, false);

INSERT INTO driver (id, identity_id, first_name, last_name, city, phone_number, picture, active, car_id,
                    duration_active, blocked, in_ride,
                    deleted)
VALUES (2, 6, 'Jovo', 'Jovic', 'Novi Sad', '067123423', 'slikaTODO', true, 2, 0, false,
        false, false);

--ROUTES
INSERT INTO route (id, deleted)
VALUES (1, false);

INSERT INTO route (id, deleted)
VALUES (2, false);

--COORDINATES
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (1, 0, 'Promenada', 45.24473173439358, 19.84146283175646, 1, true);
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (2, 1, NULL, 45.2480590388405, 19.839219531462838, 1, false);
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (3, 2, 'Balans Palacinkarnica', 45.25258638691436, 19.83754453391027, 1, true);

INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (4, 0, 'Pekara Evropa', 45.240182962558684, 19.825826428862268, 2, true);
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (5, 1, NULL, 45.2480590388405, 19.839219531462838, 2, false);
INSERT INTO coordinates(id, index, location_name, x, y, route_id, is_stop)
VALUES (6, 2, 'Medicinski Fakultet Novi Sad', 45.25328151383017, 19.824271073957725, 2, true);

--RIDES
INSERT INTO ride (id, driver_id, ride_type, start, finish, route_id, price, ride_status, deleted)
VALUES (1, 1, 0, '2022-10-23 00:00', NULL, 1, 110, 0, false);

INSERT INTO clients_rides (client_id, ride_id)
VALUES (1, 1);

--RESERVATIONS
INSERT INTO reservation (id, driver_id, ride_type, start, route_id, price, deleted)
VALUES (1, 2, 1, '2022-12-22 13:00', 2, 40, false);

INSERT INTO clients_reservations (RESERVATION_ID, CLIENT_ID)
VALUES (1, 2);

--REVIEWS
INSERT INTO review (id, driver_id, client_id, ride_id, rating, comment, deleted)
VALUES (1, 1, 1, 1, 4.8, 'Fin i kulturan vozac', false);