INSERT INTO PERSON VALUES (TIMESTAMP WITH TIME ZONE '2021-06-03 20:00:00.0000000+00', 'Peter',  45, DEFAULT);
INSERT INTO PERSON VALUES (TIMESTAMP WITH TIME ZONE '2021-06-03 20:00:00.0000000+00', 'Lois',   41, DEFAULT);
INSERT INTO PERSON VALUES (TIMESTAMP WITH TIME ZONE '2021-06-03 20:00:00.0000000+00', 'Meg',    15, DEFAULT);
INSERT INTO PERSON VALUES (TIMESTAMP WITH TIME ZONE '2021-06-03 20:00:00.0000000+00', 'Chris',  13, DEFAULT);
INSERT INTO PERSON VALUES (TIMESTAMP WITH TIME ZONE '2021-06-03 20:00:00.0000000+00', 'Brian',  6,  DEFAULT);
INSERT INTO PERSON VALUES (TIMESTAMP WITH TIME ZONE '2021-06-03 20:00:00.0000000+00', 'Stewie', 0,  DEFAULT);

INSERT INTO POINT (name, time, inserted, value) VALUES ('apples', TIMESTAMP WITH TIME ZONE '2021-07-05 11:10:00.000000+00', now(), 10);
INSERT INTO POINT (name, time, inserted, value) VALUES ('apples', TIMESTAMP WITH TIME ZONE '2021-07-05 12:00:00.000000+00', now(), 10);
INSERT INTO POINT (name, time, inserted, value) VALUES ('apples', TIMESTAMP WITH TIME ZONE '2021-07-05 12:10:00.000000+00', now(), 10);
INSERT INTO POINT (name, time, inserted, value) VALUES ('apples', TIMESTAMP WITH TIME ZONE '2021-07-07 12:00:00.000000+00', now(), 10);
INSERT INTO POINT (name, time, inserted, value) VALUES ('apples', TIMESTAMP WITH TIME ZONE '2021-07-08 12:10:00.000000+00', now(), 10);
INSERT INTO POINT (name, time, inserted, value) VALUES ('apples', TIMESTAMP WITH TIME ZONE '2021-07-08 12:10:00.000000+00', now() + INTERVAL '1 hour', 11);
INSERT INTO POINT (name, time, inserted, value) VALUES ('apples', TIMESTAMP WITH TIME ZONE '2021-07-09 12:10:00.000000+00', now(), 10);
INSERT INTO POINT (name, time, inserted, value) VALUES ('apples', TIMESTAMP WITH TIME ZONE '2021-07-10 12:10:00.000000+00', now(), 10);
