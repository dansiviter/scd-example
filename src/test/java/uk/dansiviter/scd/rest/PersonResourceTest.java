package uk.dansiviter.scd.rest;

import static jakarta.ws.rs.client.Entity.entity;
import static jakarta.ws.rs.core.HttpHeaders.ETAG;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_MODIFIED;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static uk.dansiviter.scd.hamcrest.HasRecordComponentWithValue.hasRecordComponent;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.GenericType;
import uk.dansiviter.scd.rest.api.Person;
import uk.dansiviter.scd.rest.api.PersonBuilder;

@HelidonTest
class PersonResourceTest {
	private static final String BASE = "v1alpha/people";

	@Inject
	WebTarget webTarget;

	WebTarget base() {
		return this.webTarget.path(BASE);
	}

	@Test
	void persons() {
		var actual = base().path("Glenn").request().get();
		assertThat(actual.getStatus(), is(NO_CONTENT.getStatusCode()));

		// put first 'Glenn'
		var person = PersonBuilder.builder().name("Glenn").age(61).build();
		actual = base().request().put(Entity.entity(person, APPLICATION_JSON));
		assertThat(actual.getStatus(), is(OK.getStatusCode()));
		var created0 = actual.readEntity(Person.class);
		assertThat(created0.inserted(), notNullValue());
		assertThat(actual.getHeaderString(ETAG), is(ETagFilter.toString(eTag(created0))));

		// put newer record with a change
		person = person.withAge(62);
		actual = base().request().put(Entity.entity(person, APPLICATION_JSON));
		assertThat(actual.getStatus(), is(OK.getStatusCode()));
		var created1 = actual.readEntity(Person.class);
		assertThat(created0, is(not(created1)));
		assertThat(created0.inserted(), is(not(created1.inserted())));
		assertThat(actual.getHeaderString(ETAG), is(not(ETagFilter.toString(eTag(created0)))));

		// try put, idempotent
		actual = base().request().put(Entity.entity(person, APPLICATION_JSON));
		assertThat(actual.getStatus(), is(OK.getStatusCode()));
		var created2 = actual.readEntity(Person.class);
		assertThat(created1, is(created2));
		assertThat(created1.inserted(), is(created2.inserted()));
		assertThat(actual.getHeaderString(ETAG), is(ETagFilter.toString(eTag(created1))));

		// get 'Glenn' audit
		actual = base().path("Glenn/audit").request().get();
		var audit = actual.readEntity(new GenericType<List<Person>>() {});
		assertThat(audit, hasSize(2));
		assertThat(audit.get(0).inserted(), greaterThan(audit.get(1).inserted()));

		// check latest is correct
		actual = base().path("Glenn").request().get();
		var Glenn = actual.readEntity(Person.class);
		assertThat(Glenn, is(created2));

		// check E-Tag worked
		actual = base().path("Glenn").request()
			.header("If-None-Match", eTag(Glenn))
			.get();
		assertThat(actual.getStatus(), is(NOT_MODIFIED.getStatusCode()));
		assertThat(Glenn, not(actual.hasEntity()));

		// put new person 'Jillian'
		person = person.with().name("Jillian").age(39).build();
		actual = base().request().put(entity(person, APPLICATION_JSON));
		assertThat(actual.getStatus(), is(OK.getStatusCode()));
		var jillian = actual.readEntity(Person.class);

		// get Jane
		actual = base().path("Jillian").request().get();
		assertThat(actual.readEntity(Person.class), is(jillian));

		// get latest persons
		actual = base().request().get();
		var all = actual.readEntity(new GenericType<List<Person>>() {});
		assertThat(all, hasSize(8));
		assertThat(all, hasItem(hasRecordComponent("name", is("Jillian"))));
		assertThat(all, hasItem(hasRecordComponent("name", is("Glenn"))));

		actual = base().path("audit").request().get();
		var allAudit = actual.readEntity(new GenericType<List<Person>>() {});
		assertThat(allAudit, hasSize(9));

		// check we can get value at a particular point in time (i.e. first one)
		actual = base().path("Glenn").queryParam("instant", created0.inserted()).request().get();
		assertThat(actual.readEntity(Person.class), is(created0));
	}

	private static EntityTag eTag(Person person) {
		return new EntityTag(Long.toHexString(person.hashCode()));
	}
}
