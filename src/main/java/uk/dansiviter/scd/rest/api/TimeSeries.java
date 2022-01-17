package uk.dansiviter.scd.rest.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.soabase.recordbuilder.core.RecordBuilder;
import uk.dansiviter.scd.entity.PointEntity;
import uk.dansiviter.scd.entity.TimeSeriesEntity;

@RecordBuilder
public record TimeSeries(
	UUID id,
	Instant inserted,
	@NotNull @Size(min = 3, max = 32) String name,
	@Size(min = 3, max = 128) String description,
	List<Point> points)
implements TimeSeriesBuilder.With
{
	public static TimeSeries from(TimeSeriesEntity entity, List<PointEntity> points) {
		return TimeSeriesBuilder.builder()
			.id(entity.getId())
			.name(entity.getName())
			.description(entity.getDescription())
			.inserted(entity.getInserted())
			.points(Point.from(points))
			.build();
	}
}
