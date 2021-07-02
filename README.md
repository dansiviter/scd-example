[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/dansiviter/scd-example/Build?style=flat-square)](https://github.com/dansiviter/scd-example/actions/workflows/build.yaml) ![Java 16](https://img.shields.io/badge/-Java%2016%2B-informational?style=flat-square)

# Slowly Changing Dimension (SCD) Example #

This example shows using a very simplistic SCD Type 2 persistence using JPA. This demonstrates:
* Latest Person by name,
* Person by name at a point-in-time (instant),
* Person audit trail,
* All latest Persons,
* All Person audit trail (rarely used, but useful example non-the-less).

For simplicity sake, this doesn't use start/end dates, it just uses a inserted value as part of the key.

Notes:
* It seems EclipseLink refuses to compile with Java 16. It's due to a really old version of ASM and that's not going to be updated until 3.0.1 (aka Jakarta). Workaround is to use bump to just `org.eclipse.persistence:org.eclipse.persistence.asm:3.0.1`.
* Regardless of what H2 [documentation states](http://www.h2database.com/html/datatypes.html#timestamp_with_time_zone_type), it doesn't work with `java.time.Instant` (or it could be PIBCAK!). So I had to create a attribute converter to do this,
* `E-Tag` is generated from the hashcode of the key of an entity. For a collection of entities it gets all the keys and creates it from them. The potential for collision is probably not wonderful, but for the purposes of this it's adequate',
* If you want to view your data, go to [`localhost:8082`](http://localhost:8082) and use JDBC URL `jdbc:h2:mem:app`,
* Some of the queries use correlated subqueries, which _can_ be a concern but this can be mitigated with indexes on the correlation criteria and some DBs can rewrite them.


```
docker run `
  -e POSTGRES_PASSWORD=pwd `
  -p 5432:5432 `
	-it `
	--rm `
	postgres:alpine
```


```
docker run `
  -e SQLPAD_CONNECTIONS__pgdemo__name=Postgres `
  -e SQLPAD_CONNECTIONS__pgdemo__driver=postgres `
  -e SQLPAD_CONNECTIONS__pgdemo__host=host.docker.internal `
  -e SQLPAD_CONNECTIONS__pgdemo__database=postgres `
  -e SQLPAD_CONNECTIONS__pgdemo__username=postgres `
  -e SQLPAD_CONNECTIONS__pgdemo__password=pwd `
	-e SQLPAD_AUTH_DISABLED=true `
	-e SQLPAD_AUTH_DISABLED_DEFAULT_ROLE=editor `
	-p 3000:3000 `
	-it `
	--rm `
	sqlpad/sqlpad
```

```
docker run -p 8082:80 `
	-e 'PGADMIN_DEFAULT_EMAIL=user@domain.com' `
	-e 'PGADMIN_DEFAULT_PASSWORD=SuperSecret' `
	-it `
	--rm `
	-d dpage/pgadmin4
```


CREATE TABLE IF NOT EXISTS test.metric
(
    id uuid NOT NULL,
    inserted timestamp with time zone NOT NULL,
    name character varying(32) COLLATE pg_catalog."default",
    CONSTRAINT metric_pkey PRIMARY KEY (id, inserted)
)

CREATE TABLE IF NOT EXISTS test.point
(
    metric_id uuid,
    "time" timestamp with time zone,
    inserted timestamp with time zone,
    value bigint,
    PRIMARY KEY (metric_id, "time", inserted)
) PARTITION BY RANGE ("time");


INSERT INTO test.point (metric_id, "time", inserted, value)
    SELECT (
      SELECT id FROM test.metric WHERE name = "carrots"
    ), ts, now(), 10
    FROM generate_series(
      '2021-06-07'::timestamp,
      '2021-06-08'::timestamp,
      '1 second'::interval ts
    )
