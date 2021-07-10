package uk.dansiviter.scd.repo;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.OffsetDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class InstantConverter implements AttributeConverter<Instant, OffsetDateTime> {
	@Override
	public OffsetDateTime convertToDatabaseColumn(Instant attribute) {
		return attribute != null ? attribute.atOffset(UTC) : null;
	}

	@Override
	public Instant convertToEntityAttribute(OffsetDateTime dbData) {
		return dbData != null ? dbData.toInstant() : null;
	}
}
