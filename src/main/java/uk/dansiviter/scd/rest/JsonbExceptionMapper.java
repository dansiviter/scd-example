package uk.dansiviter.scd.rest;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static uk.dansiviter.scd.Pair.pair;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import uk.dansiviter.scd.Pair;

@Provider
public class JsonbExceptionMapper extends AbstractExceptionMapper<ProcessingException> {
	@Override
	protected Pair<String, Response> extract(ProcessingException ex) {
		return pair(ex.getMessage(), Response.status(BAD_REQUEST).build());
	}
}
