package uk.dansiviter.scd.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.threeten.extra.PeriodDuration;

import uk.dansiviter.scd.repo.TimeSeriesRepo;
import uk.dansiviter.scd.rest.api.Point;
import uk.dansiviter.scd.rest.api.Window;

@Path("v1alpha/timeseries")
@Produces(APPLICATION_JSON)
public class TimeSeriesResource {
	@Inject
	private TimeSeriesRepo repo;

	@GET
	@Path("{timeSeriesId}")
	public List<Point> points(@PathParam("timeSeriesId") UUID timeSeriesId) {
		return Point.from(repo.points(timeSeriesId));
	}

	@GET
	@Path("{name}/windows")
	public List<Window> windows(
			@PathParam("name") @Size(min = 3, max = 128) String timeSeriesName,
			@QueryParam("start") Optional<Temporal> start,
			@QueryParam("end") Optional<Temporal> end,
			@QueryParam("alignment") @DefaultValue("P1D") PeriodDuration alignment)
	{
		return repo.window(timeSeriesName, start, end, alignment).stream().map(Window::to).collect(Collectors.toList());
	}
}
