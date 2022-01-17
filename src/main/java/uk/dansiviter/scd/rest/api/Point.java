package uk.dansiviter.scd.rest.api;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.soabase.recordbuilder.core.RecordBuilder;
import uk.dansiviter.scd.entity.PointEntity;

@RecordBuilder
public record Point(
	@NotNull
	String timeSeriesName,
	@NotNull
	Instant time,
	Instant inserted,
	@Min(0)
	Long value)
implements PointBuilder.With
{
	public static Point from(PointEntity entity) {
		return PointBuilder.builder()
			.timeSeriesName(entity.getTimeSeriesName())
			.time(entity.getTime())
			.value(entity.getValue())
			.inserted(entity.getInserted())
			.build();
	}

	public static List<Point> from(List<PointEntity> entities) {
		return entities.stream().map(Point::from).collect(Collectors.toList());
	}
}
