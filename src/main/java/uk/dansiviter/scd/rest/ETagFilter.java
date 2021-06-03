package uk.dansiviter.scd.rest;

import static javax.ws.rs.core.Response.Status.OK;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;

import uk.dansiviter.scd.repo.KeyUtil;

@ETag
@Provider
public class ETagFilter implements ContainerResponseFilter {
	@Inject
	private KeyUtil keyUtil;

	@Override
	public void filter(ContainerRequestContext req, ContainerResponseContext res) {
		if (res.getStatus() != OK.getStatusCode()) {
			return;
		}

		var eTag = new EntityTag(this.keyUtil.eTag(res.getEntity()));

		var builder = req.getRequest().evaluatePreconditions(eTag);
		if (builder != null) {
			copy(builder, res);
		} else {
			res.getHeaders().add(HttpHeaders.ETAG, eTag.getValue());
		}
	}

	private static void copy(ResponseBuilder builder, ContainerResponseContext res) {
		var response = builder.build();
		res.setEntity(null);
		res.setStatusInfo(response.getStatusInfo());
		response.getHeaders().forEach(res.getHeaders()::put);
	}
}
