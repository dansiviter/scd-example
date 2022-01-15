package uk.dansiviter.scd.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.threeten.extra.PeriodDuration;

import uk.dansiviter.scd.entity.Window;
import uk.dansiviter.scd.repo.TimeSeriesRepo;
import uk.dansiviter.scd.rest.api.Point;
import uk.dansiviter.scd.rest.api.TimeSeries;

@Path("v1alpha/timeseries")
@Produces(APPLICATION_JSON)
public class TimeSeriesResource {
	@Inject
	private TimeSeriesRepo repo;

	@GET
	public List<TimeSeries> timeSeries() {
		return TimeSeries.from(repo.timeSeries());
	}

	@GET
	@Path("{timeSeriesId}")
	public List<Point> points(@PathParam("timeSeriesId") UUID timeSeriesId) {
		return Point.from(repo.points(timeSeriesId));
	}

	@GET
	@Path("{timeSeriesId}/windows")
	public List<Window> windows(
			@PathParam("timeSeriesId") UUID timeSeriesId,
			@QueryParam("start") Optional<Temporal> start,
			@QueryParam("end") Optional<Temporal> end,
			@QueryParam("alignment") @DefaultValue("P1D") PeriodDuration alignment)
	{
		return repo.window(timeSeriesId, start, end, alignment);
	}
}
