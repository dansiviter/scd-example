package uk.dansiviter.scd.rest.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import uk.dansiviter.scd.entity.PointEntity;

@RecordBuilder
public record Point(
	@NotNull
	Instant time,
	@Min(0)
	BigDecimal value,
	Optional<Instant> inserted)
implements PointBuilder.With {

	public static Point from(PointEntity entity) {
		return PointBuilder.builder()
			.time(entity.getTime())
			.value(entity.getValue())
			.inserted(Optional.of(entity.getInserted()))
			.build();
	}

	public static List<Point> from(List<PointEntity> entities) {
		return entities.stream().map(Point::from).collect(Collectors.toList());
	}
}
