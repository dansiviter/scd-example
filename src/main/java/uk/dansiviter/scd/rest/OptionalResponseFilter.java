package uk.dansiviter.scd.rest;

import static javax.ws.rs.Priorities.ENTITY_CODER;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

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
