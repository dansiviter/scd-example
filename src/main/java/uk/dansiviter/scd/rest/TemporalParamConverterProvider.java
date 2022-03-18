package uk.dansiviter.scd.rest;

import static uk.dansiviter.scd.rest.FunctionalParamConverter.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Objects;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TemporalParamConverterProvider implements ParamConverterProvider {

	@Override
	@SuppressWarnings("unchecked")
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if (rawType.equals(Instant.class)) {
			return (ParamConverter<T>) converter(Instant::parse, Objects::toString);
		}
		if (rawType.equals(Temporal.class)) {
			return (ParamConverter<T>) converter(TemporalParamConverterProvider::parse, Objects::toString);
		}
		return null;
	}

	private static Temporal parse(String value) {
		try {
			return Instant.parse(value);
		} catch (DateTimeParseException e) {
			// do nothing
		}
		try {
			return LocalDate.parse(value);
		} catch (DateTimeParseException e) {
			// do nothing
		}
		throw new DateTimeException("Unable to parse! [" + value + "]");
	}
}
