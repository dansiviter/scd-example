package uk.dansiviter.scd.rest;

import static uk.dansiviter.scd.rest.FunctionalParamConverter.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Duration;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.threeten.extra.PeriodDuration;

@Provider
public class TemporalAmountParamConverterProvider implements ParamConverterProvider {
	@Override
	@SuppressWarnings("unchecked")
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if (rawType.equals(Duration.class)) {
			return (ParamConverter<T>) converter(Duration::parse, Duration::toString);
		}
		if (rawType.equals(PeriodDuration.class)) {
			return (ParamConverter<T>) converter(PeriodDuration::parse, PeriodDuration::toString);
		}
		return null;
	}
}
