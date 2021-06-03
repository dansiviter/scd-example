package uk.dansiviter.scd.repo;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import uk.dansiviter.scd.entity.Person;

@ApplicationScoped
public class PersonRepo {
	@PersistenceContext
	private EntityManager em;

	public List<Person> all(Optional<Instant> instant) {
		TypedQuery<Person> query = instant
			.map(i -> em.createNamedQuery("Person.all.instant", Person.class).setParameter("instant", i))
			.orElse(em.createNamedQuery("Person.all", Person.class));
		return query.getResultList();
	}

	public List<Person> allAudit() {
		return em.createNamedQuery("Person.allAudit", Person.class).getResultList();
	}

	public Person get(String name, Optional<Instant> instant) {
		try {
			TypedQuery<Person> query = instant
				.map(i -> em.createNamedQuery("Person.find.instant", Person.class).setParameter("instant", i))
				.orElse(em.createNamedQuery("Person.find", Person.class));
			return query.setParameter("name", name)
				.setMaxResults(1)
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Person> getAudit(String name) {
		return em.createNamedQuery("Person.find", Person.class)
			.setParameter("name", name)
			.getResultList();
	}

	@Transactional
	public Person persist(Person person) {
		var prev = get(person.getName(), Optional.empty());
		if (person.equals(prev)) {
			return prev;
		}
		person.setInserted(Instant.now());
		em.persist(person);
		return person;
	}
}
