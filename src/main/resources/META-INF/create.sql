ALTER DATABASE test SET TIMEZONE TO 'utc';
INSERT INTO PERSON (uuid, name, age, inserted) VALUES ('3a4789f6-b4a9-4944-89b8-83181767694c', 'Peter',  45, now());
INSERT INTO PERSON (uuid, name, age, inserted) VALUES ('d42e5380-ea8f-49c5-9605-d41a6ae6e424', 'Lois',   41, now());
INSERT INTO PERSON (uuid, name, age, inserted) VALUES ('59d87205-5eaf-4405-81fa-add3042773a5', 'Meg',    15, now());
INSERT INTO PERSON (uuid, name, age, inserted) VALUES ('eae0a447-0852-4494-8e2f-093a4e76794d', 'Chris',  13, now());
INSERT INTO PERSON (uuid, name, age, inserted) VALUES ('2bcceabb-2d41-485e-b3d2-cf526aa217ce', 'Brian',  6,  now());
INSERT INTO PERSON (uuid, name, age, inserted) VALUES ('9b85c68b-54ed-46af-9bc9-1647a8f82a7a', 'Stewie', 0,  now());

INSERT INTO TIME_SERIES (id, inserted, name, description) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', now(), 'apples', NULL);
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-06-30 00:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-05 11:10:00.123456+00', 10, now());
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-05 12:00:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-05 12:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-07 12:00:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-08 12:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-08 12:10:00.000000+00', 11, now() + INTERVAL '1 hour');
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-09 12:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-10 12:10:00.000000+00', 10, now());
INSERT INTO POINT (timeseriesid, time, value, inserted) VALUES ('1ec70869-b2d3-605a-8597-72abdfca6147', TIMESTAMPTZ '2021-07-11 23:10:00.000000+00', 10, now());
