package uk.dansiviter.scd.repo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import uk.dansiviter.scd.entity.Point;
import uk.dansiviter.scd.entity.Window;

@ApplicationScoped
public class PointRepo {
	@PersistenceContext
	private EntityManager em;

	public List<Point> points(String name) {
		return em.createNamedQuery("Point.allByName", Point.class)
			.setParameter(1, name)
			.getResultList();
	}

	public List<Window> window(String name, Optional<Instant> start, Optional<Instant> end) {
		return em.createNamedQuery("Point.window", Window.class)
			.setParameter(1, Timestamp.from(start.orElseGet(() -> start(name))))
			.setParameter(2, Timestamp.from(end.orElseGet(() -> end(name))))
			.setParameter(3, name)
			.getResultList();
	}

	private Instant start(String name) {
		return em.createQuery("SELECT MIN(p.time) FROM Point p WHERE p.name = ?1", Instant.class)
			.setParameter(1, name)
			.getSingleResult();
	}

	private Instant end(String name) {
		return em.createQuery("SELECT MAX(p.time) FROM Point p WHERE p.name = ?1", Instant.class)
		.setParameter(1, name)
		.getSingleResult();
	}
}
