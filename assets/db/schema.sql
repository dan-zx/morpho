CREATE TABLE station (
  id        INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  name      VARCHAR2 (100) NOT NULL,
  radius    FLOAT NOT NULL,
  latitude  NUMBER NOT NULL,
  longitude NUMBER NOT NULL,
  CONSTRAINT station_un UNIQUE (latitude, longitude)
);

CREATE TABLE route (
  id         INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  station_id INTEGER NOT NULL,
  name       VARCHAR2 (100) NOT NULL,
  CONSTRAINT route_station_fk FOREIGN KEY (station_id) REFERENCES station (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE schedule (
  id          	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  route_id    	INTEGER NOT NULL,
  departure_at	TIME NOT NULL,
  service_day 	VARCHAR2 (50) NOT NULL,
  CONSTRAINT schedule_route_fk FOREIGN KEY (route_id) REFERENCES route (id) ON DELETE CASCADE ON UPDATE CASCADE
);