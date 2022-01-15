package uk.dansiviter.scd.rest.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.soabase.recordbuilder.core.RecordBuilder;
import uk.dansiviter.scd.entity.TimeSeriesEntity;

@RecordBuilder
public record TimeSeries(
	UUID id,
	Instant inserted,
	@NotNull @Size(min = 3, max = 32) String name,
	@Size(min = 3, max = 128) String description)
implements TimeSeriesBuilder.With
{
	public static TimeSeries from(TimeSeriesEntity entity) {
		return TimeSeriesBuilder.builder()
			.id(entity.getId())
			.name(entity.getName())
			.description(entity.getDescription())
			.inserted(entity.getInserted())
			.build();
	}

	public static List<TimeSeries> from(List<TimeSeriesEntity> entities) {
		return entities.stream().map(TimeSeries::from).collect(Collectors.toList());
	}
}
