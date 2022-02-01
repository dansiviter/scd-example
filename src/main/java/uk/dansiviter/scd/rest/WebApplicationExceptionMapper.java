package uk.dansiviter.scd.rest;

import static java.lang.String.format;
import static uk.dansiviter.scd.Pair.pair;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import uk.dansiviter.scd.Pair;

@Provider
public class WebApplicationExceptionMapper extends AbstractExceptionMapper<WebApplicationException> {

	protected Pair<String, Response> extract(WebApplicationException ex) {
		return pair(message(ex), ex.getResponse());
	}

	private static String message(WebApplicationException e) {
		return format("HTTP %s: %s", e.getResponse().getStatusInfo(), e.getMessage());
	}
}
