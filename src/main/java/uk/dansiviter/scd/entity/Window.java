package uk.dansiviter.scd.entity;

import java.time.Instant;
import java.time.OffsetDateTime;

import javax.json.bind.annotation.JsonbPropertyOrder;

@JsonbPropertyOrder({ "start", "end", "value" })
public class Window {
	private final Instant start;
	private final Instant end;
	private final Long value;

	public Window(OffsetDateTime start, OffsetDateTime end, Long value) {
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

	public Long getValue() {
		return value;
	}
}
