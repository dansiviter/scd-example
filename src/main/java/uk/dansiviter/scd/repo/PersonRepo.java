package uk.dansiviter.scd.repo;

import java.time.Instant;
import java.util.List;

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

	private TypedQuery<Person> baseGet(String name) {
		return em.createNamedQuery("Person.find", Person.class)
			.setParameter("name", name);
	}

	public Person get(String name) {
		try {
			return baseGet(name).setMaxResults(1).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Person> getAudit(String name) {
		return baseGet(name).getResultList();
	}

	@Transactional
	public Person persist(Person person) {
		var prev = get(person.getName());
		if (person.equals(prev)) {
			return prev;
		}
		person.setCreated(Instant.now());
		em.persist(person);
		return person;
	}
}
