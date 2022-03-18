package uk.dansiviter.scd.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.Family;
import jakarta.ws.rs.ext.ExceptionMapper;
import uk.dansiviter.scd.Pair;
import uk.dansiviter.scd.ScdLog;

public abstract class AbstractExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {
	@Inject
	private ScdLog log;

	@Override
	public Response toResponse(E ex) {
		var pair = extract(ex);
		if (pair.second().getStatusInfo().getFamily() == Family.CLIENT_ERROR) {
			log.clientError(() -> pair.first());
		} else {
			log.serverError(() -> pair.first(), ex);
		}
		return pair.second();
	}

	protected abstract Pair<String, Response> extract(E ex);
}
