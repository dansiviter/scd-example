package uk.dansiviter.scd.rest;

import java.util.function.Function;

import javax.ws.rs.ext.ParamConverter;

public class FunctionalParamConverter<T> implements ParamConverter<T> {
	private final Function<String, T> from;
	private final Function<T, String> to;

	FunctionalParamConverter(Function<String, T> from,  Function<T, String> to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public T fromString(String value) {
		if (value == null) {
			return null;
		}
		return this.from.apply(value);
	}

	@Override
	public String toString(T value) {
		if (value == null) {
			return null;
		}
		return this.to.apply(value);
	}

	public static <T> ParamConverter<T> converter(Function<String, T> from, Function<T, String> to) {
		return new FunctionalParamConverter<>(from, to);
	}
}
