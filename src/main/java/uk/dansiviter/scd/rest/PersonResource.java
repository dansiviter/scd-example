package uk.dansiviter.scd.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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

import uk.dansiviter.scd.repo.PersonRepo;
import uk.dansiviter.scd.rest.api.Person;

@Path("v1alpha/people")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class PersonResource {
	@Inject
	private PersonRepo repo;

	@GET
	@ETag
	public List<Person> all(@QueryParam("instant") Optional<Instant> instant) {
		return Person.from(repo.all(instant));
	}

	@GET
	@Path("audit")
	public List<Person> allAudit() {
		return Person.from(repo.allAudit());
	}

	@GET
	@Path("{name}")
	@ETag
	public Optional<Person> get(
		@PathParam("name") String name,
		@QueryParam("instant") Optional<Instant> instant)
	{
		return repo.get(name, instant).map(Person::from);
	}

	@GET
	@Path("{name}/audit")
	public List<Person> audit(@PathParam("name") String name) {
		return Person.from(repo.getAudit(name));
	}

	@PUT
	@ETag
	public Person put(Person person) {
		return Person.from(repo.persist(person.toEntity()));
	}
}
