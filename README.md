[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/dansiviter/scd-example/Build?style=flat-square)](https://github.com/dansiviter/scd-example/actions/workflows/build.yaml) ![Java 16](https://img.shields.io/badge/-Java%2016-informational?style=flat-square)

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
	-d `
	--rm `
	postgres:alpine
```

```
docker run -p 8082:80 `
	-e 'PGADMIN_DEFAULT_EMAIL=user@domain.com' `
	-e 'PGADMIN_DEFAULT_PASSWORD=SuperSecret' `
	-d `
	--rm `
	-d dpage/pgadmin4
```
