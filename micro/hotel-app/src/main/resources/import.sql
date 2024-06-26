create sequence hotel_sequence start with 1 increment by 1;
INSERT INTO Hotel(id, travelorderid,nights) VALUES(nextval('hotel_sequence'),1,5);
