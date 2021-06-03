package uk.dansiviter.scd.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class TemporalParamConverterProvider implements ParamConverterProvider {
	@Override
	@SuppressWarnings("unchecked")
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
			if (rawType.equals(Instant.class))
					return (ParamConverter<T>) new InstantConverter();
			return null;
	}

	private static class InstantConverter implements ParamConverter<Instant> {
		@Override
		public Instant fromString(String value) {
			if (value == null) {
				return null;
			}
			return Instant.parse(value);
		}

		@Override
		public String toString(Instant value) {
			if (value == null) {
				return null;
			}
			return value.toString();
		}
	}
}
