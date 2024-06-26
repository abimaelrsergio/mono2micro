create sequence flight_sequence start with 1 increment by 1;
INSERT INTO Flight(id, travelorderid, fromAirport, toAirport) VALUES (nextval('flight_sequence'), 1, 'GRU', 'MCO');
