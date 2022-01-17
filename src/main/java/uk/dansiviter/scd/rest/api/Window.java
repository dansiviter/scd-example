package uk.dansiviter.scd.rest.api;

import java.time.Instant;

import uk.dansiviter.scd.entity.WindowEntity;

public record Window (Instant start, Instant end, Long value) {
	public static Window to(WindowEntity w) {
		return new Window(w.getStart(), w.getEnd(), w.getValue());
	}
}
