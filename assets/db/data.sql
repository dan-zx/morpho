INSERT INTO station (name, radius, latitude, longitude) VALUES ('Estacionamiento 1', 12, 19.055437, -98.283104);
INSERT INTO station (name, radius, latitude, longitude) VALUES ('Bernal', 5, 19.051764, -98.280779);
INSERT INTO station (name, radius, latitude, longitude) VALUES ('Gaos', 5, 19.051983, -98.284442);
INSERT INTO station (name, radius, latitude, longitude) VALUES ('Gimnasio', 5, 19.054582, -98.286278);
INSERT INTO station (name, radius, latitude, longitude) VALUES ('Salida principal', 5, 19.056670, -98.283709);

INSERT INTO route (station_id, name) VALUES (1, 'CAPU');
INSERT INTO route (station_id, name) VALUES (1, 'PUEBLA');
INSERT INTO route (station_id, name) VALUES (1, 'CIRCUITO');

INSERT INTO route (station_id, name) VALUES (2, 'CAPU');
INSERT INTO route (station_id, name) VALUES (2, 'PUEBLA');
INSERT INTO route (station_id, name) VALUES (2, 'CIRCUITO');

INSERT INTO route (station_id, name) VALUES (3, 'CAPU');
INSERT INTO route (station_id, name) VALUES (3, 'PUEBLA');
INSERT INTO route (station_id, name) VALUES (3, 'CIRCUITO');

INSERT INTO route (station_id, name) VALUES (4, 'CAPU');
INSERT INTO route (station_id, name) VALUES (4, 'PUEBLA');
INSERT INTO route (station_id, name) VALUES (4, 'CIRCUITO');

INSERT INTO route (station_id, name) VALUES (5, 'CAPU');
INSERT INTO route (station_id, name) VALUES (5, 'PUEBLA');
INSERT INTO route (station_id, name) VALUES (5, 'CIRCUITO');

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

INSERT INTO schedule (route_id, departure_at, service_day) VALUES (4, '06:22', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (4, '17:42', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (5, '06:01', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (5, '07:31', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (5, '14:16', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (5, '17:41', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (6, '06:01', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (6, '07:31', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (6, '14:16', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (6, '17:41', 'WEEKDAY');

INSERT INTO schedule (route_id, departure_at, service_day) VALUES (7, '06:23', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (7, '17:43', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (8, '06:02', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (8, '07:32', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (8, '14:17', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (8, '17:42', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (9, '06:02', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (9, '07:32', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (9, '14:17', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (9, '17:42', 'WEEKDAY');

INSERT INTO schedule (route_id, departure_at, service_day) VALUES (10, '06:24', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (10, '17:44', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (11, '06:04', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (11, '07:34', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (11, '14:19', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (11, '17:44', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (12, '06:04', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (12, '07:34', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (12, '14:19', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (12, '17:44', 'WEEKDAY');

INSERT INTO schedule (route_id, departure_at, service_day) VALUES (13, '06:25', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (13, '17:45', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (14, '06:05', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (14, '07:35', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (14, '14:20', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (14, '17:45', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (15, '06:05', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (15, '07:35', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (15, '14:20', 'WEEKDAY');
INSERT INTO schedule (route_id, departure_at, service_day) VALUES (15, '17:45', 'WEEKDAY');