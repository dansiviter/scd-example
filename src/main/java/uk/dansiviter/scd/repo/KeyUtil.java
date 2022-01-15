package uk.dansiviter.scd.repo;

import static java.lang.Long.toHexString;
import static java.util.stream.StreamSupport.stream;

import java.util.Optional;
import java.util.OptionalLong;

import javax.enterprise.context.ApplicationScoped;

import uk.dansiviter.scd.rest.api.Cachable;

@ApplicationScoped
public class KeyUtil {
	// @PersistenceUnit
	// private EntityManagerFactory emf;

	public OptionalLong hash(Object entity) {
		if (entity instanceof Iterable) {
			return OptionalLong.of(hash((Iterable<?>) entity));
		}
		if (entity instanceof Optional) {
			entity = ((Optional<?>) entity).orElse(null);
		}
		if (entity instanceof Cachable) {
			return OptionalLong.of(((Cachable) entity).hash());
		}
		if (entity == null) {
			return OptionalLong.empty();
		}
		throw new UnsupportedOperationException("Unsupported type! [" + entity + "]");
		// return emf.getPersistenceUnitUtil().getIdentifier(entity).hashCode();
	}

	private long hash(Iterable<?> entities) {
		// var puu = emf.getPersistenceUnitUtil();
		return  stream(entities.spliterator(), false)
			.mapToLong(v -> hash(v).getAsLong())
			.reduce(7L, (i, j) -> 31 *  i + j);
	}

	public String eTag(Iterable<?> entities) {
		return toHexString(hash(entities));
	}

	public Optional<String> eTag(Object entity) {
		var value = hash(entity);
		if (value.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(toHexString(value.getAsLong()));
	}
}
