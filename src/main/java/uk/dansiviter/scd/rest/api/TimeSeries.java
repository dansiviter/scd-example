package uk.dansiviter.scd.rest.api;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uk.dansiviter.scd.entity.PointEntity;
import uk.dansiviter.scd.entity.TimeSeriesEntity;
import uk.dansiviter.scd.rest.validation.NoDuplicates;

@RecordBuilder
public record TimeSeries(
	@NotNull @Size(min = 3, max = 32) String name,
	@Size(min = 3, max = 128) String description,
	@NotNull @NotEmpty @NoDuplicates List<Point> points,
	Optional<Instant> inserted)
implements TimeSeriesBuilder.With {

	public static TimeSeries from(TimeSeriesEntity timeSeries, List<PointEntity> points) {
		return TimeSeriesBuilder.builder()
			.name(timeSeries.getName())
			.description(timeSeries.getDescription())
			.inserted(Optional.of(timeSeries.getInserted()))
			.points(Point.from(points))
			.build();
	}

	public static TimeSeriesEntity toEntity(TimeSeries timeSeries) {
		var entity = new TimeSeriesEntity();
		entity.setName(timeSeries.name());
		entity.setDescription(timeSeries.description());
		return entity;
	}
}
