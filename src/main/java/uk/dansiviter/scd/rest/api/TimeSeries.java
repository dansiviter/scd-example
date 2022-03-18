package uk.dansiviter.scd.rest.api;

import java.time.Instant;
import java.util.List;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uk.dansiviter.scd.rest.validation.NoDuplicates;

@RecordBuilder
public record TimeSeries(
	@NotNull @Size(min = 3, max = 32) String name,
	Instant inserted,
	@Size(min = 3, max = 128) String description,
	@NotNull @NotEmpty @NoDuplicates List<Point> points)
implements TimeSeriesBuilder.With { }
