package uk.dansiviter.scd.repo;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.h2.api.TimestampWithTimeZone;
import org.h2.util.DateTimeUtils;

@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, TimestampWithTimeZone> {
	@Override
	public TimestampWithTimeZone convertToDatabaseColumn(Instant attribute) {
		if (attribute == null) {
			return null;
		}
		var dateTime = attribute.atZone(UTC);
		var date = dateTime.toLocalDate();
		var time = dateTime.toLocalTime();
		var dateMillis = DateTimeUtils.dateValue(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
		return new TimestampWithTimeZone(dateMillis, time.toNanoOfDay(), (short) 0);
	}

	@Override
	public Instant convertToEntityAttribute(TimestampWithTimeZone dbData) {
		if (dbData == null) {
			return null;
		}
		var date = LocalDate.of(dbData.getYear(), dbData.getMonth(), dbData.getDay());
		var time = LocalTime.ofNanoOfDay(dbData.getNanosSinceMidnight());
		return date.atTime(time).atZone(UTC).toInstant();
	}
}
