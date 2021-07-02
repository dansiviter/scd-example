package uk.dansiviter.scd.rest;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import uk.dansiviter.scd.ScdLog;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
	@Inject
	private ScdLog log;

	@Override
	public Response toResponse(WebApplicationException ex) {
		var family = ex.getResponse().getStatusInfo().getFamily();
		if (family == Family.CLIENT_ERROR) {
			log.clientError(() -> message(ex));
		} else {
			log.serverError(() -> message(ex), ex);
		}
		return ex.getResponse();
	}

	private static String message(WebApplicationException e) {
		return String.format("HTTP %s: %s", e.getResponse().getStatusInfo(), e.getMessage());
	}
}
