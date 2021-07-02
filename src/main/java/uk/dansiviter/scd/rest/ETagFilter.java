package uk.dansiviter.scd.rest;

import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.PUT;
import static javax.ws.rs.core.HttpHeaders.ETAG;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;

import uk.dansiviter.scd.repo.KeyUtil;

@ETag
@Provider
public class ETagFilter implements ContainerResponseFilter {
	private static final Set<String> METHODS = Set.of(GET, PUT);

	@Inject
	private KeyUtil keyUtil;

	@Override
	public void filter(ContainerRequestContext req, ContainerResponseContext res) {
		if (!METHODS.contains(req.getMethod()) || res.getStatus() != OK.getStatusCode()) {
			return;
		}

		var eTag = new EntityTag(this.keyUtil.eTag(res.getEntity()));
		var builder = req.getRequest().evaluatePreconditions(eTag);
		if (builder != null) {
			copy(builder, res);
		} else {
			res.getHeaders().add(ETAG, eTag.toString());
		}
	}

	private static void copy(ResponseBuilder builder, ContainerResponseContext res) {
		var response = builder.build();
		res.setEntity(null);
		res.setStatusInfo(response.getStatusInfo());
		response.getHeaders().forEach(res.getHeaders()::put);
	}
}
