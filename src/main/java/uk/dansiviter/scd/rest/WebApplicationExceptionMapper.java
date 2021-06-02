package uk.dansiviter.scd.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
	private static final Logger LOG = Logger.getLogger(WebApplicationExceptionMapper.class.getName());

	@Override
	public Response toResponse(WebApplicationException exception) {
		LOG.log(Level.WARNING, "WAE", exception);
		return exception.getResponse();
	}
}
