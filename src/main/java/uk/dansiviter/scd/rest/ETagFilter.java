package uk.dansiviter.scd.rest;

import static jakarta.ws.rs.HttpMethod.GET;
import static jakarta.ws.rs.HttpMethod.PUT;
import static jakarta.ws.rs.Priorities.HEADER_DECORATOR;
import static jakarta.ws.rs.core.HttpHeaders.ETAG;
import static jakarta.ws.rs.core.Response.Status.OK;

import java.util.Set;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.EntityTag;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.RuntimeDelegate;
import uk.dansiviter.scd.repo.KeyUtil;

@ETag
@Provider
@Priority(HEADER_DECORATOR)
public class ETagFilter implements ContainerResponseFilter {
	private static final Set<String> METHODS = Set.of(GET, PUT);

	@Inject
	private KeyUtil keyUtil;

	@Override
	public void filter(ContainerRequestContext req, ContainerResponseContext res) {
		if (!METHODS.contains(req.getMethod()) || res.getStatus() != OK.getStatusCode()) {
			return;
		}

		var eTag = this.keyUtil.eTag(res.getEntity()).map(EntityTag::new);
		eTag.ifPresent(e -> evaluate(req, res, e));
	}

	private static void evaluate(ContainerRequestContext req, ContainerResponseContext res, EntityTag eTag) {
		var builder = req.getRequest().evaluatePreconditions(eTag);
		if (builder != null) {
			copy(builder, res);
		} else {
			res.getHeaders().add(ETAG, toString(eTag));
		}
	}

	private static void copy(ResponseBuilder builder, ContainerResponseContext res) {
		var response = builder.build();
		res.setEntity(null);
		res.setStatusInfo(response.getStatusInfo());
		response.getHeaders().forEach(res.getHeaders()::put);
	}

	static String toString(EntityTag eTag) {
		return RuntimeDelegate.getInstance().createHeaderDelegate(EntityTag.class).toString(eTag);
	}
}
