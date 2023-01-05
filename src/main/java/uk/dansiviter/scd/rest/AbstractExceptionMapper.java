package uk.dansiviter.scd.rest;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static uk.dansiviter.scd.rest.api.ApiError.apiError;

import java.util.List;
import java.util.Optional;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.Family;
import jakarta.ws.rs.core.Variant;
import jakarta.ws.rs.ext.ExceptionMapper;
import uk.dansiviter.scd.Pair;
import uk.dansiviter.scd.ScdLog;
import uk.dansiviter.scd.rest.api.ApiError;

public abstract class AbstractExceptionMapper<E extends Throwable> implements ExceptionMapper<E> {
	private static final List<Variant> variants = Variant.mediaTypes(APPLICATION_JSON_TYPE).build();

	@Inject
	private ScdLog log;
	@Context
	private Provider<Request> request;

	@Override
	public Response toResponse(E ex) {
		var pair = extract(ex);
		ApiError error;
		if (pair.second().getStatusInfo().getFamily() == Family.CLIENT_ERROR) {
			log.clientError(() -> pair.first());
			error = apiError(Optional.of(pair.first()));
		} else {
			log.serverError(() -> pair.first(), ex);
			error = apiError();
		}

		return Response
			.status(pair.second().getStatusInfo())
			.entity(error)
			.variants(variants)
			.build();
	}

	protected abstract Pair<String, Response> extract(E ex);
}
