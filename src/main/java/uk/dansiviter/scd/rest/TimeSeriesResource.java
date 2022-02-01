package uk.dansiviter.scd.rest;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.threeten.extra.PeriodDuration;

import uk.dansiviter.scd.entity.PointEntity;
import uk.dansiviter.scd.entity.TimeSeriesEntity;
import uk.dansiviter.scd.repo.TimeSeriesRepo;
import uk.dansiviter.scd.rest.api.Point;
import uk.dansiviter.scd.rest.api.TimeSeries;
import uk.dansiviter.scd.rest.api.Window;

@Path("v1alpha/timeseries")
@Produces(APPLICATION_JSON)
public class TimeSeriesResource {
	@Inject
	private TimeSeriesRepo repo;

	@GET
	public List<TimeSeries> timeSeries() {
		var stream = repo.timeSeries().stream();
		return stream.map(this::map).collect(toList());
	}

	@PUT
	public TimeSeries timeseries(@Valid TimeSeries timeSeries) {
		var pointEntities = timeSeries.points().stream().map(p -> PointEntity.from(timeSeries, p)).collect(toList());
		var pair = repo.persist(TimeSeriesEntity.from(timeSeries), pointEntities);
		return pair.first().toRecord(pair.second());
	}

	private TimeSeries map(TimeSeriesEntity ts) {
		var points = repo.points(ts.getName());
		return ts.toRecord(points);
	}

	@GET
	@Path("{timeSeriesName}/points")
	public List<Point> points(@PathParam("timeSeriesName") String timeSeriesName) {
		return PointEntity.from(repo.points(timeSeriesName));
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
