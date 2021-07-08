package uk.dansiviter.scd.repo;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfNextYear;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.threeten.extra.PeriodDuration;

import uk.dansiviter.scd.ScdLog;
import uk.dansiviter.scd.entity.Point;
import uk.dansiviter.scd.entity.Window;

@ApplicationScoped
public class PointRepo {
	@Inject
	private ScdLog log;
	@PersistenceContext
	private EntityManager em;

	public List<Point> points(String name) {
		return em.createNamedQuery("Point.allByName", Point.class)
			.setParameter(1, name)
			.getResultList();
	}

	public List<Window> window(String name, Optional<Temporal> start, Optional<Temporal> end, PeriodDuration alignment) {
		var startInstant = start(name, start, alignment);
		var endInstant = end(name, startInstant, end, alignment);
		this.log.window(name, startInstant, endInstant, alignment);
		return em.createNamedQuery("Point.window", Window.class)
			.setParameter(1, startInstant)
			.setParameter(2, endInstant)
			.setParameter(3, name)
			.setParameter(4, alignment.toString())
			.getResultList();
	}

	private OffsetDateTime start(String name, Optional<Temporal> start, PeriodDuration alignment) {
		if (start.isPresent()) {
			return start.map(PointRepo::toDateTime).orElseThrow();
		}

		var result = em.createQuery("SELECT MIN(p.time) FROM Point p WHERE p.name = ?1", Instant.class)
			.setParameter(1, name)
			.getSingleResult()
			.atOffset(UTC);

		result = round(result, lastWholeUnit(alignment), true);
		return result;
	}

	private OffsetDateTime end(String name, OffsetDateTime start, Optional<Temporal> end, PeriodDuration alignment) {
		if (end.isPresent()) {
			return end.map(PointRepo::toDateTime).orElseThrow();
		}

		var result = em.createQuery("SELECT MAX(p.time) FROM Point p WHERE p.name = ?1", Instant.class)
			.setParameter(1, name)
			.getSingleResult()
			.atOffset(UTC);

		return result;
	}

	private static OffsetDateTime toDateTime(Temporal t) {
		if (t instanceof LocalDate) {
			return ((LocalDate) t).atTime(OffsetTime.of(0, 0, 0, 0, UTC));
		} else if (t instanceof Instant) {
			return ((Instant) t).atOffset(UTC);
		} else if (t instanceof OffsetDateTime) {
			return (OffsetDateTime) t;
		}
		throw new IllegalArgumentException("Not expected! [" + t + "]");
	}

	private static OffsetDateTime round(OffsetDateTime in, ChronoUnit unit, boolean down) {
		if (unit == ChronoUnit.NANOS) {
			return in;
		}
		// Instants don't like truncation, so have to use a offset date time
		var out = in;
		if (unit == ChronoUnit.MONTHS) {
			out = out.truncatedTo(ChronoUnit.DAYS).with(down ? firstDayOfMonth() : firstDayOfNextMonth());
		} else if (unit == ChronoUnit.YEARS) {
			out = out.truncatedTo(ChronoUnit.DAYS).with(down ? firstDayOfYear() : firstDayOfNextYear());
		} else {
			out = out.plus(down ? 0 : 1, unit);
		}
		return out;
	}

	private static ChronoUnit lastWholeUnit(PeriodDuration duration) {
		if (duration.getDuration().toNanosPart() != 0) {
			return ChronoUnit.NANOS;
		}
		if (duration.getDuration().toSecondsPart() != 0) {
			return ChronoUnit.SECONDS;
		}
		if (duration.getDuration().toMinutesPart() != 0) {
			return ChronoUnit.MINUTES;
		}
		if (duration.getDuration().toHoursPart() != 0) {
			return ChronoUnit.HOURS;
		}
		if (duration.getPeriod().getDays() != 0) {
			return ChronoUnit.DAYS;
		}
		if (duration.getPeriod().getMonths() != 0) {
			return ChronoUnit.MONTHS;
		}
		return ChronoUnit.YEARS;
	}
}
