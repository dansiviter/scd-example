package uk.dansiviter.scd.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;

public class WindowEntity {
	private final Instant start;
	private final Instant end;
	private final BigDecimal value;

	public WindowEntity(OffsetDateTime start, OffsetDateTime end, BigDecimal value) {
		this.start = start.toInstant();
		this.end = end.toInstant();
		this.value = value;
	}

	public Instant getStart() {
		return start;
	}

	public Instant getEnd() {
		return end;
	}

	public BigDecimal getValue() {
		return value;
	}
}
