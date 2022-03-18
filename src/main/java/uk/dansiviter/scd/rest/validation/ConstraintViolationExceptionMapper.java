package uk.dansiviter.scd.rest.validation;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static uk.dansiviter.scd.Pair.pair;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import uk.dansiviter.scd.Pair;
import uk.dansiviter.scd.rest.AbstractExceptionMapper;

@Provider
public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper<ConstraintViolationException> {

	@Override
	protected Pair<String, Response> extract(ConstraintViolationException ex) {
		return pair(
			prepareMessage(ex),
			Response.status(BAD_REQUEST).build()
		);
	}

  private static String prepareMessage(ConstraintViolationException ex) {
		var buf = new StringBuilder("Validation failed! [");
		ex.getConstraintViolations().forEach(cv -> append(buf, cv));
		buf.setLength(buf.length() - 1);  // trim last comma
		return buf.append(']').toString();
  }

	private static void append(StringBuilder buf, ConstraintViolation<?> cv) {
		buf.append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(',');
	}
}
