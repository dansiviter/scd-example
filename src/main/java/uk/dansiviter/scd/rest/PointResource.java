package uk.dansiviter.scd.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import uk.dansiviter.scd.entity.Point;
import uk.dansiviter.scd.entity.Window;
import uk.dansiviter.scd.repo.PointRepo;

@Path("v1alpha/points")
@Produces(APPLICATION_JSON)
public class PointResource {
	@Inject
	private PointRepo repo;

	@GET
	@Path("{name}")
	public List<Point> points(
			@PathParam("name") String name) {
		return repo.points(name);
	}

	@GET
	@Path("{name}/windows")
	public List<Window> windows(
			@PathParam("name") String name) {
		return repo.window(name, Optional.empty(), Optional.empty());
	}
}
