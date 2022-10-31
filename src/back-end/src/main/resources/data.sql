--ADMINS
INSERT INTO admin (id, email, first_name, last_name, password, city, phone_number, picture, deleted)
VALUES (1, 'asi@maildrop.cc', 'Andrej', 'Culjak', 'asi123', 'Novi Sad', '0677777778', 'slikaTODO', false);

INSERT INTO admin (id, email, first_name, last_name, password, city, phone_number, picture, deleted)
VALUES (2, 'dmitar@maildrop.cc', 'Dimitrije', 'Petrov', 'dmitar123', 'Novi Sad', '067123456', 'slikaTODO', false);

--CLIENTS
INSERT INTO client (id, email, first_name, last_name, password, city, phone_number, picture, verified, blocked, in_ride,
                    deleted)
VALUES (1, 'pero@maildrop.cc', 'Pero', 'Peric', 'pero123', 'Novi Sad', '0691777778', 'slikaTODO', true, false, false,
        false);

INSERT INTO client (id, email, first_name, last_name, password, city, phone_number, picture, verified, blocked, in_ride,
                    deleted)
VALUES (2, 'jovo@maildrop.cc', 'Jovo', 'Jovic', 'jovo123', 'Novi Sad', '067123423', 'slikaTODO', true, false, false,
        false);

--CARS
INSERT INTO car (id, car_type, x, y, deleted)
VALUES (1, 0, 45.24533662754101, 19.8430497771721, false);

INSERT INTO car (id, car_type, x, y, deleted)
VALUES (2, 0, 45.24533662754101, 19.8430497771721, false);

INSERT INTO CAR_ADDITIONAL_SERVICES (car_id, ADDITIONAL_SERVICES)
VALUES (1, 'Ljubimci do 15kg');

--DRIVERS
INSERT INTO driver (id, email, first_name, last_name, password, city, phone_number, picture, active, car_id,
                    duration_active, blocked, in_ride,
                    deleted)
VALUES (1, 'pero@maildrop.cc', 'Pero', 'Peric', 'pero123', 'Novi Sad', '0691777778', 'slikaTODO', true, 1, 0, false,
        false, false);

INSERT INTO driver (id, email, first_name, last_name, password, city, phone_number, picture, active, car_id,
                    duration_active, blocked, in_ride,
                    deleted)
VALUES (2, 'jovo@maildrop.cc', 'Jovo', 'Jovic', 'jovo123', 'Novi Sad', '067123423', 'slikaTODO', true, 2, 0, false,
        false, false);

--RIDES
INSERT INTO ride (id, driver_id, ride_type, start, finish, price, ride_status, deleted)
VALUES (1, 1, 0, '2022-10-23 00:00', NULL, 110,0,false );

INSERT INTO clients_rides (client_id, ride_id) VALUES (1,1);