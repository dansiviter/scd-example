package uk.dansiviter.scd.rest;

import static jakarta.ws.rs.Priorities.ENTITY_CODER;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

import java.io.IOException;
import java.util.Optional;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Handles {@link Optional} responses.
 */
@Provider
@Priority(ENTITY_CODER)
public class OptionalResponseFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException {
			var entity = res.getEntity();
			if (entity != null && entity instanceof Optional) {
				var optional = (Optional<?>) entity;
				optional.ifPresentOrElse(res::setEntity, () -> noContent(res));
			}
	}

	private static void noContent(ContainerResponseContext res) {
		res.setStatusInfo(NO_CONTENT);
		res.setEntity(null);
	}
}
