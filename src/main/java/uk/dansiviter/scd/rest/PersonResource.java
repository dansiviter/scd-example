package uk.dansiviter.scd.rest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import uk.dansiviter.scd.entity.Person;
import uk.dansiviter.scd.repo.PersonRepo;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
	@Inject
	private PersonRepo repo;

	@GET
	public List<Person> all(
		@QueryParam("instant") Instant instant)  // eclipse-ee4j/jersey#4799
	{
		return repo.all(Optional.ofNullable(instant));
	}

	@GET
	@Path("audit")
	public List<Person> allAudit() {
		return repo.allAudit();
	}

	@GET
	@Path("{name}")
	@ETag
	public Person get(
		@PathParam("name") String name,
		@QueryParam("instant") Instant instant)  // eclipse-ee4j/jersey#4799
	{
		return repo.get(name, Optional.ofNullable(instant));
	}

	@GET
	@Path("{name}/audit")
	public List<Person> audit(@PathParam("name") String name) {
		return repo.getAudit(name);
	}

	@PUT
	@ETag
	public Person put(Person person) {
		return repo.persist(person);
	}
}
