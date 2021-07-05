package uk.dansiviter.scd.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Duration;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class DurationParamConverterProvider implements ParamConverterProvider {
	@Override
	@SuppressWarnings("unchecked")
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if (rawType.equals(Duration.class)) {
			return (ParamConverter<T>) new DurationConverter();
		}
		return null;
	}

	private static class DurationConverter implements ParamConverter<Duration> {
		@Override
		public Duration fromString(String value) {
			return Duration.parse(value);
		}

		@Override
		public String toString(Duration value) {
			return value.toString();
		}
	}
}
