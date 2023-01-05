package uk.dansiviter.scd.rest;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
	public Person put(@Valid Person body) {
		return Person.from(repo.persist(body.toEntity()));
	}


	public static void main(String[] args) {
		var jsonb = JsonbBuilder.create();
		var myObject = new MyObject(Optional.of("foo"));
		var json = jsonb.toJson(myObject);
		System.out.println(json);
		myObject = jsonb.fromJson(json, MyObject.class);
		System.out.println("str1=" + myObject.str1().get());
	}

	public static record MyObject(Optional<String> str1) {
		public MyObject() {
			this(Optional.empty());
		}
	}
}
