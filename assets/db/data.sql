INSERT INTO station (name, radius, latitude, longitude) VALUES ('Estacionamiento 1', 12, 19.055437, -98.283104);
INSERT INTO station (name, radius, latitude, longitude) VALUES ('Bernal', 5, 19.051764, -98.280779);
INSERT INTO station (name, radius, latitude, longitude) VALUES ('Gaos', 5, 19.051983, -98.284442);
INSERT INTO station (name, radius, latitude, longitude) VALUES ('Gimnasio', 5, 19.054582, -98.286278);
INSERT INTO station (name, radius, latitude, longitude) VALUES ('Salida principal', 5, 19.056670, -98.283709);

INSERT INTO route (station_id, name) VALUES (1, 'CAPU');
INSERT INTO route (station_id, name) VALUES (1, 'PUEBLA');
INSERT INTO route (station_id, name) VALUES (1, 'CIRCUITO');

INSERT INTO schedule (route_id, departure_at, service_day) VALUES (1, '06:20', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (1, '17:40', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (2, '06:00', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (2, '07:30', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (2, '14:15', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (2, '17:40', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (3, '06:00', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (3, '07:30', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (3, '14:15', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (3, '17:40', 'WEEKDAY');