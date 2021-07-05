package uk.dansiviter.scd.entity;

import java.sql.Timestamp;
import java.time.Instant;

import uk.dansiviter.scd.repo.InstantConverter;

public class Window {
	private final Instant start;
	private final Instant end;
	private final Long value;

	public Window(Timestamp start, Timestamp end, Long value) {
		this.start = InstantConverter.from(start);
		this.end = InstantConverter.from(end);
		this.value = value;
	}

	public Instant getStart() {
		return start;
	}

	public Instant getEnd() {
		return end;
	}

	public Long getValue() {
		return value;
	}
}
