package uk.dansiviter.scd.rest;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response.Status;
import uk.dansiviter.scd.rest.api.PointBuilder;
import uk.dansiviter.scd.rest.api.TimeSeries;
import uk.dansiviter.scd.rest.api.TimeSeriesBuilder;
import uk.dansiviter.scd.rest.api.Window;

@HelidonTest
@Configuration(configSources = "application-dbTest.yaml")
class TimeSeriesResourceTest {
	private static final String BASE = "v1alpha/timeseries";

	@Inject
	WebTarget webTarget;

	WebTarget base() {
		return this.webTarget.path(BASE);
	}

	@Test
	void getAll() {
		var actual = base().request().get();
		assertThat(actual.getStatus(), is(OK.getStatusCode()));

		var timeseries = actual.readEntity(new GenericType<List<TimeSeries>>() {});

		assertThat(timeseries, hasSize(2));
	}

	@Test
	void getWindows() {
		var actual = base().path("apples/windows").request().get();
		assertThat(actual.getStatus(), is(OK.getStatusCode()));

		var windows = actual.readEntity(new GenericType<List<Window>>() {});

		assertThat(windows, hasSize(12));
		assertThat(windows.get(5), is(new Window(
			Instant.parse("2021-07-05T00:10:00Z"),
			Instant.parse("2021-07-06T00:10:00Z"),
			new BigDecimal("30.00"))));
	}

	@Test
	void persist() {
		var timeSeries = TimeSeriesBuilder.builder()
			.name("oranges")
			.points(List.of(PointBuilder.builder()
					.time(Instant.now())
					.value(BigDecimal.valueOf(1))
					.build()))
			.build();
		var response = base().request().put(Entity.entity(timeSeries, APPLICATION_JSON));

		assertThat(response.getStatus(), is(Status.OK.getStatusCode()));
		var actual = response.readEntity(TimeSeries.class);
		assertThat(actual.inserted(), notNullValue());
		assertThat(actual.points().get(0).inserted(), notNullValue());
	}

	@Test
	void persist_duplicates() {
		var point = PointBuilder.builder()
			.time(Instant.now())
			.value(BigDecimal.valueOf(1))
			.build();
		var timeSeries = TimeSeriesBuilder.builder()
			.name("oranges")
			.points(List.of(point, point))
			.build();
		var response = base().request().put(Entity.entity(timeSeries, APPLICATION_JSON));

		assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
	}

	@Test
	void persist_nullPoints() {
		var timeSeries = TimeSeriesBuilder.builder().name("oranges").build();
		var response = base().request().put(Entity.entity(timeSeries, APPLICATION_JSON));

		assertThat(response.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));
	}
}
