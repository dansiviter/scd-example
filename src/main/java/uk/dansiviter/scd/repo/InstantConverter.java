package uk.dansiviter.scd.repo;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, Timestamp> {
	@Override
	public Timestamp convertToDatabaseColumn(Instant attribute) {
		return attribute != null ? Timestamp.from(attribute) : null;
	}

	@Override
	public Instant convertToEntityAttribute(Timestamp dbData) {
		return dbData != null ? dbData.toInstant() : null;
	}
}
