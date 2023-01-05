package uk.dansiviter.scd.rest.api;

import static java.lang.Float.floatToIntBits;
import static java.lang.Integer.toUnsignedString;
import static java.lang.Math.random;

import java.util.Optional;

public record ApiError(String id, Optional<String> msg) {
	public static ApiError apiError() {
		return apiError(Optional.empty());
	}

	public static ApiError apiError(Optional<String> msg) {
		return new ApiError(toUnsignedString(floatToIntBits((float) random()), 36), msg);
	}
}
