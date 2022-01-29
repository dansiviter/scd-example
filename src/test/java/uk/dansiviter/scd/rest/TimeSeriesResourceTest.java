package uk.dansiviter.scd.rest;

import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import uk.dansiviter.scd.rest.api.Window;

@HelidonTest
class TimeSeriesResourceTest {
	private static final String BASE = "v1alpha/timeseries";

	@Inject
	WebTarget webTarget;

	WebTarget base() {
		return this.webTarget.path(BASE);
	}

	@Test
	void get() {
		var actual = base().path("apples/windows").request().get();
		assertThat(actual.getStatus(), is(OK.getStatusCode()));

		var windows = actual.readEntity(new GenericType<List<Window>>() {});

		assertThat(windows, hasSize(12));
		assertThat(windows.get(5), is(new Window(Instant.parse("2021-07-05T00:10:00Z"), Instant.parse("2021-07-06T00:10:00Z"), new BigDecimal("30.00"))));
	}
}
