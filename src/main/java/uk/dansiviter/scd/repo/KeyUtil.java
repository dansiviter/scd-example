package uk.dansiviter.scd.repo;

import static java.lang.Long.toHexString;
import static java.util.stream.StreamSupport.stream;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class KeyUtil {
	@PersistenceUnit
	private EntityManagerFactory emf;

	public long hash(Object entity) {
		return emf.getPersistenceUnitUtil().getIdentifier(entity).hashCode();
	}

	public long hash(Iterable<?> entities) {
		var puu = emf.getPersistenceUnitUtil();
		return stream(entities.spliterator(), false)
			.map(puu::getIdentifier)
			.mapToLong(Objects::hashCode)
			.reduce(7L, (i, j) -> 31 *  i + j);
	}

	public String eTag(Iterable<?> entities) {
		return toHexString(hash(entities));
	}

	public String eTag(Object entity) {
		return toHexString(hash(entity));
	}
}
