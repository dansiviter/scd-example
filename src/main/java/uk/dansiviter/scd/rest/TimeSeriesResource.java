package uk.dansiviter.scd.rest;

import static java.util.stream.Collectors.toList;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.threeten.extra.PeriodDuration;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
	public List<TimeSeries> get() {
		var stream = repo.timeSeries().stream();
		return stream.map(this::map).collect(toList());
	}

	@PUT
	public TimeSeries put(@Valid TimeSeries body) {
		var pointEntities = body.points().stream().map(p -> PointEntity.from(body, p)).collect(toList());
		var pair = repo.persist(TimeSeries.toEntity(body), pointEntities);
		return TimeSeries.from(pair.first(), pair.second());
	}

	private TimeSeries map(TimeSeriesEntity ts) {
		var points = repo.points(ts.getName());
		return  TimeSeries.from(ts, points);
	}

	@GET
	@Path("{timeSeriesName}/points")
	public List<Point> points(@PathParam("timeSeriesName") String timeSeriesName) {
		return Point.from(repo.points(timeSeriesName));
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
