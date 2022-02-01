package uk.dansiviter.scd.rest.api;

import java.math.BigDecimal;
import java.time.Instant;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Point(
	@NotNull
	Instant time,
	Instant inserted,
	@Min(0)
	BigDecimal value)
implements PointBuilder.With { }
