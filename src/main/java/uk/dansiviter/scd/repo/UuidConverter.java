package uk.dansiviter.scd.repo;

import static org.postgresql.util.ByteConverter.int8;

import java.util.UUID;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UuidConverter implements AttributeConverter<UUID, Object> {
	@Override
	public Object convertToDatabaseColumn(UUID attribute) {
		return attribute;
	}

	@Override
	public UUID convertToEntityAttribute(Object dbData) {
		if (dbData == null) {
			return null;
		}
		if (dbData instanceof UUID) {
			return (UUID) dbData;
		}
		if (dbData instanceof byte[]) {  // in some random cases PosgreSQL returns as byte array
			var bytes = (byte[]) dbData;
			return new UUID(int8(bytes, 0), int8(bytes, 8));
		}
		throw new IllegalArgumentException("Unable to convert! [" + dbData + "]");
	}
}
