package uk.dansiviter.scd.rest.api;

import static java.lang.Float.floatToIntBits;
import static java.lang.Integer.toUnsignedString;
import static java.lang.Math.random;

public record Error(String id) {
	public Error() {
		this(toUnsignedString(floatToIntBits((float) random()), 36));
	}
}
