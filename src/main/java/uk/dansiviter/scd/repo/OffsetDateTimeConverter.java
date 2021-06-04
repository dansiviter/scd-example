package uk.dansiviter.scd.repo;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.h2.api.TimestampWithTimeZone;
import org.h2.util.DateTimeUtils;

@Converter(autoApply = true)
public class OffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, TimestampWithTimeZone> {
	@Override
	public TimestampWithTimeZone convertToDatabaseColumn(OffsetDateTime attribute) {
		if (attribute == null) {
			return null;
		}
		var date = attribute.toLocalDate();
		var time = attribute.toLocalTime();
		var dateMillis = DateTimeUtils.dateValue(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
		return new TimestampWithTimeZone(dateMillis, time.toNanoOfDay(), attribute.getOffset().getTotalSeconds());
	}

	@Override
	public OffsetDateTime convertToEntityAttribute(TimestampWithTimeZone dbData) {
		if (dbData == null) {
			return null;
		}
		var date = LocalDate.of(dbData.getYear(), dbData.getMonth(), dbData.getDay());
		var time = LocalTime.ofNanoOfDay(dbData.getNanosSinceMidnight());
		return date.atTime(time)
			.atOffset(ZoneOffset.ofTotalSeconds(dbData.getTimeZoneOffsetSeconds()));
	}
}
