package uk.dansiviter.scd.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import uk.dansiviter.scd.entity.Person;
import uk.dansiviter.scd.repo.PersonRepo;

@Path("/persons")
public class PersonResource {
	@Inject
	private PersonRepo repo;

	@GET
	@Path("{name}")
	public Person get(@PathParam("name") String name) {
		return repo.get(name);
	}

	@GET
	@Path("{name}/audit")
	public List<Person> audit(@PathParam("name") String name) {
		return repo.getAudit(name);
	}

	@PUT
	public Person put(Person person) {
		return repo.persist(person);
	}
}
