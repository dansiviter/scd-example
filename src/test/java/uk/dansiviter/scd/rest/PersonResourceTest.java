package uk.dansiviter.scd.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import uk.dansiviter.scd.entity.Person;

@HelidonTest
class PersonResourceTest {
	@Inject
	WebTarget webTarget;

	@Test
	void hello() {
		var actual = webTarget.path("/persons/Bob").request().get();
		assertThat(actual.getStatus(), is(Status.NO_CONTENT.getStatusCode()));

		var person = new Person();
		person.setName("Bob");
		person.setAge(1);
		actual = webTarget.path("/persons").request().put(Entity.entity(person, MediaType.APPLICATION_JSON));
		assertThat(actual.getStatus(), is(Status.OK.getStatusCode()));
		var created0 = actual.readEntity(Person.class);
		assertThat(created0.getCreated(), notNullValue());

		actual = webTarget.path("/persons").request().put(Entity.entity(person, MediaType.APPLICATION_JSON));
		assertThat(actual.getStatus(), is(Status.OK.getStatusCode()));
		var created1 = actual.readEntity(Person.class);
		assertThat(created0, is(created1));
		assertThat(created0.getCreated(), is(created1.getCreated()));

		person.setAge(2);
		actual = webTarget.path("/persons").request().put(Entity.entity(person, MediaType.APPLICATION_JSON));
		assertThat(actual.getStatus(), is(Status.OK.getStatusCode()));
		var created2 = actual.readEntity(Person.class);
		assertThat(created0, is(not(created2)));
		assertThat(created0.getCreated(), is(not(created2.getCreated())));

		actual = webTarget.path("/persons/Bob/audit").request().get();
		var audit = actual.readEntity(new GenericType<List<Person>>() {});
		assertThat(audit, hasSize(2));
	}
}
