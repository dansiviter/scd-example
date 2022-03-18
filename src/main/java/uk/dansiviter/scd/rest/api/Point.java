package uk.dansiviter.scd.rest.api;

import java.math.BigDecimal;
import java.time.Instant;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RecordBuilder
public record Point(
	@NotNull
	Instant time,
	Instant inserted,
	@Min(0)
	BigDecimal value)
implements PointBuilder.With { }
