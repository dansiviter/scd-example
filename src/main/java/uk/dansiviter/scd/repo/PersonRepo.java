package uk.dansiviter.scd.repo;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import uk.dansiviter.scd.entity.PersonEntity;

@ApplicationScoped
public class PersonRepo {
	@PersistenceContext
	private EntityManager em;

	public List<PersonEntity> all(Optional<Instant> instant) {
		var query = instant
			.map(i -> em.createNamedQuery("Person.all.instant", PersonEntity.class).setParameter("instant", i))
			.orElse(em.createNamedQuery("Person.all", PersonEntity.class));
		return query.getResultList();
	}

	public List<PersonEntity> allAudit() {
		return em.createNamedQuery("Person.all.audit", PersonEntity.class).getResultList();
	}

	public Optional<PersonEntity> get(String name, Optional<Instant> instant) {
		try {
			var query = instant
				.map(i ->
					em.createNamedQuery("Person.find.instant", PersonEntity.class)
						.setParameter("2", i.atOffset(ZoneOffset.UTC)))
				.orElse(em.createNamedQuery("Person.find", PersonEntity.class));
			return Optional.ofNullable(query
				.setParameter(1, requireNonNull(name))
				.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	public List<PersonEntity> getAudit(String name) {
		return em.createNamedQuery("Person.find.audit", PersonEntity.class)
			.setParameter(1, requireNonNull(name))
			.getResultList();
	}

	@Transactional
	public PersonEntity persist(PersonEntity person) {
		var prev = get(person.getName(), Optional.empty());
		if (prev.isPresent() && person.equals(prev.get())) {
			return prev.get();
		}
		em.persist(person);
		em.flush();
		return person;
	}
}
