package uk.dansiviter.scd.repo;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import uk.dansiviter.scd.entity.PersonEntity;

@ApplicationScoped
public class PersonRepo {
	@PersistenceContext
	private EntityManager em;

	public List<PersonEntity> all(Optional<Instant> instant) {
		var query = instant
			.map(i -> em.createNamedQuery("Person.all.instant", PersonEntity.class).setParameter("instant", i))
			.orElse(em.createNamedQuery("Person.all.native", PersonEntity.class));
		return query.getResultList();
	}

	public List<PersonEntity> allAudit() {
		return em.createNamedQuery("Person.allAudit", PersonEntity.class).getResultList();
	}

	public Optional<PersonEntity> get(String name, Optional<Instant> instant) {
		try {
			var query = instant
				.map(i -> em.createNamedQuery("Person.find.instant", PersonEntity.class).setParameter("instant", i))
				.orElse(em.createNamedQuery("Person.find", PersonEntity.class));
			return Optional.ofNullable(query.setParameter("name", name).getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	public List<PersonEntity> getAudit(String name) {
		return em.createNamedQuery("Person.audit", PersonEntity.class)
			.setParameter("name", requireNonNull(name))
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
