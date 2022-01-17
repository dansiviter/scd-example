ALTER DATABASE test SET TIMEZONE TO 'utc';
INSERT INTO PERSON (name, age, inserted) VALUES ('Peter',  45, now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Lois',   41, now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Meg',    15, now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Chris',  13, now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Brian',  6,  now());
INSERT INTO PERSON (name, age, inserted) VALUES ('Stewie', 0,  now());

INSERT INTO TIME_SERIES (id, inserted, name, description) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', now(), 'apples', NULL);
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-06-30 00:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-05 11:10:00.123456+00', 10, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-05 12:00:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-05 12:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-07 12:00:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-08 12:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-08 12:10:00.000000+00', 11, now() + INTERVAL '1 hour');
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-09 12:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-10 12:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesname, time, value, inserted) VALUES ('apples', TIMESTAMPTZ '2021-07-11 23:10:00.000000+00', 10, now());
