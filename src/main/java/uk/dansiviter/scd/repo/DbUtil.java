package uk.dansiviter.scd.repo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public enum DbUtil { ;
	/**
	 * @return PostgreSQL precision instant.
	 */
	public static Instant nowInstant() {
		return Instant.now().truncatedTo(ChronoUnit.MICROS);
	}
}
