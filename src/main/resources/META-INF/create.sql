ALTER DATABASE test SET TIMEZONE TO 'utc';

INSERT INTO PERSON (name, age, inserted) VALUES ('Peter',  45, now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Lois',   41, now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Meg',    15, now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Chris',  13, now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Brian',  6,  now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Stewie', 0,  now());

INSERT INTO TIME_SERIES (name, inserted, description) VALUES ('apples', now(),                     NULL);
INSERT INTO TIME_SERIES (name, inserted, description) VALUES ('apples', now() + INTERVAL '1 hour', NULL);
INSERT INTO TIME_SERIES (name, inserted, description) VALUES ('oranges', now() + INTERVAL '1 hour', NULL);

INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-06-30T00:10:00Z',          10,    now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-05 11:10:00.123456+00', 10,    now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-05T12:00:00Z',          10,    now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-05T12:10:00Z',          10.00, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-07T12:00:00.000000Z',   10,    now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-08T12:10:00Z',          10,    now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-08T12:10:00Z',          11,    now() + INTERVAL '1 hour');
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-09T12:10:00Z',          10,    now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-10T12:10:00Z',          10,    now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', '2021-07-11T23:10:00Z',          10,    now());
