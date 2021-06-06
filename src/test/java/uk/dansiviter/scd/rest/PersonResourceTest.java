package uk.dansiviter.scd.rest;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.HttpHeaders.ETAG;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_MODIFIED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;

import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import uk.dansiviter.scd.entity.Person;

@HelidonTest
class PersonResourceTest {
	@Inject
	WebTarget webTarget;

	@Test
	void persons() {
		var actual = webTarget.path("/persons/Glenn").request().get();
		assertThat(actual.getStatus(), is(NO_CONTENT.getStatusCode()));

		// put first 'Glenn'
		var person = new Person();
		person.setName("Glenn");
		person.setAge(61);
		actual = webTarget.path("/persons").request().put(Entity.entity(person, APPLICATION_JSON));
		assertThat(actual.getStatus(), is(OK.getStatusCode()));
		var created0 = actual.readEntity(Person.class);
		assertThat(created0.getInserted(), notNullValue());
		assertThat(actual.getHeaderString(ETAG), is(eTag(created0).getValue()));

		// put newer record with a change
		person.setAge(62);
		actual = webTarget.path("/persons").request().put(Entity.entity(person, APPLICATION_JSON));
		assertThat(actual.getStatus(), is(OK.getStatusCode()));
		var created1 = actual.readEntity(Person.class);
		assertThat(created0, is(not(created1)));
		assertThat(created0.getInserted(), is(not(created1.getInserted())));
		assertThat(actual.getHeaderString(ETAG), is(not(eTag(created0).getValue())));

		// try put, idempotent
		actual = webTarget.path("/persons").request().put(Entity.entity(person, APPLICATION_JSON));
		assertThat(actual.getStatus(), is(OK.getStatusCode()));
		var created2 = actual.readEntity(Person.class);
		assertThat(created1, is(created2));
		assertThat(created1.getInserted(), is(created2.getInserted()));
		assertThat(actual.getHeaderString(ETAG), is(eTag(created1).getValue()));

		// get 'Glenn' audit
		actual = webTarget.path("/persons/Glenn/audit").request().get();
		var audit = actual.readEntity(new GenericType<List<Person>>() {});
		assertThat(audit, hasSize(2));
		assertThat(audit.get(0).getInserted(), greaterThan(audit.get(1).getInserted()));

		// check latest is correct
		actual = webTarget.path("/persons/Glenn").request().get();
		var Glenn = actual.readEntity(Person.class);
		assertThat(Glenn, is(created2));

		// check E-Tag worked
		actual = webTarget.path("/persons/Glenn").request()
			.header("If-None-Match", eTag(Glenn))
			.get();
		assertThat(actual.getStatus(), is(NOT_MODIFIED.getStatusCode()));
		assertThat(Glenn, not(actual.hasEntity()));

		// put new person 'Jillian'
		person.setName("Jillian");
		person.setAge(39);
		actual = webTarget.path("/persons").request().put(entity(person, APPLICATION_JSON));
		assertThat(actual.getStatus(), is(OK.getStatusCode()));
		var jillian = actual.readEntity(Person.class);

		// get Jane
		actual = webTarget.path("/persons/Jillian").request().get();
		assertThat(actual.readEntity(Person.class), is(jillian));

		// get latest persons
		actual = webTarget.path("/persons").request().get();
		var all = actual.readEntity(new GenericType<List<Person>>() {});
		assertThat(all, hasSize(8));
		assertThat(all, hasItem(hasProperty("name", is("Jillian"))));
		assertThat(all, hasItem(hasProperty("name", is("Glenn"))));

		actual = webTarget.path("/persons/audit").request().get();
		var allAudit = actual.readEntity(new GenericType<List<Person>>() {});
		assertThat(allAudit, hasSize(9));

		// check we can get value at a particular point in time (i.e. first one)
		actual = webTarget.path("/persons/Glenn").queryParam("instant", created0.getInserted()).request().get();
		assertThat(actual.readEntity(Person.class), is(created0));
	}

	private static EntityTag eTag(Person person) {
		return new EntityTag(Long.toHexString(person.toId().hashCode()));
	}
}
