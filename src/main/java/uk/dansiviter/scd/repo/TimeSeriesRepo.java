package uk.dansiviter.scd.repo;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.util.stream.Collectors.toList;
import static uk.dansiviter.scd.Pair.pair;

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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.threeten.extra.PeriodDuration;

import uk.dansiviter.scd.Pair;
import uk.dansiviter.scd.ScdLog;
import uk.dansiviter.scd.entity.PointEntity;
import uk.dansiviter.scd.entity.TimeSeriesEntity;
import uk.dansiviter.scd.entity.WindowEntity;

@ApplicationScoped
public class TimeSeriesRepo {
	@Inject
	private ScdLog log;
	@PersistenceContext
	private EntityManager em;

	public List<TimeSeriesEntity> timeSeries() {
		return em.createNamedQuery("TimeSeries.all", TimeSeriesEntity.class)
			.getResultList();
	}

	public List<PointEntity> points(String timeSeriesName) {
		return em.createNamedQuery("Point.allByTimeSeriesName", PointEntity.class)
			.setParameter(1, timeSeriesName)
			.getResultList();
	}

	private Optional<TimeSeriesEntity> find(TimeSeriesEntity timeSeries) {
		try {
			return Optional.of(em.createNamedQuery("TimeSeries.find", TimeSeriesEntity.class)
				.setParameter("name", timeSeries.getName())
				.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	private Optional<PointEntity> find(PointEntity point) {
		try {
			return Optional.of(em.createNamedQuery("Point.find", PointEntity.class)
				.setParameter(1, point.getTimeSeriesName())
				.setParameter(2, point.getTime().atOffset(UTC))
				.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	public List<WindowEntity> window(String name, Optional<Temporal> start, Optional<Temporal> end, PeriodDuration alignment) {
		var startInstant = start(name, start, alignment);
		var endInstant = end(name, startInstant, end, alignment);
		this.log.window(name, startInstant, endInstant, alignment);

		// EclipseLink does not support named parameters!
		return em.createNamedQuery("Point.window", WindowEntity.class)
			.setParameter(1, startInstant)
			.setParameter(2, endInstant)
			.setParameter(3, name)
			.setParameter(4, alignment.toString())
			.getResultList();
	}

	@Transactional
	public Pair<TimeSeriesEntity, List<PointEntity>> persist(TimeSeriesEntity timeSeries, List<PointEntity> points) {
		return pair(
			persist(timeSeries),
			points.stream().map(this::persist).collect(toList()));
	}

	private TimeSeriesEntity persist(TimeSeriesEntity timeSeries) {
		var entity = find(timeSeries);
		if (!entity.isPresent() || !entity.get().equals(timeSeries)) {
			em.persist(timeSeries);
			return timeSeries;
		}
		return entity.get();
	}

	private PointEntity persist(PointEntity point) {
		var entity = find(point);
		if (!entity.isPresent() || !entity.get().equals(point)) {
			em.persist(point);
			return point;
		}
		return entity.get();
	}

	private OffsetDateTime start(String name, Optional<Temporal> start, PeriodDuration alignment) {
		if (start.isPresent()) {
			return start.map(TimeSeriesRepo::toDateTime).orElseThrow();
		}

		var result = em.createNamedQuery("Point.minTime", Instant.class)
			.setParameter("name", name)
			.getSingleResult()
			.atOffset(UTC);

		result = truncateTo(result, lastWholeUnit(alignment));
		return result;
	}

	private OffsetDateTime end(String name, OffsetDateTime start, Optional<Temporal> end, PeriodDuration alignment) {
		if (end.isPresent()) {
			return end.map(TimeSeriesRepo::toDateTime).orElseThrow();
		}

		var result = em.createNamedQuery("Point.maxTime", Instant.class)
			.setParameter("name", name)
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

	private static OffsetDateTime truncateTo(OffsetDateTime in, ChronoUnit unit) {
		if (unit == ChronoUnit.NANOS) {
			return in;
		}
		// Instants don't like truncation, so have to use a offset date time
		var out = in;
		if (unit == ChronoUnit.MONTHS) {
			out = out.truncatedTo(ChronoUnit.DAYS).with(firstDayOfMonth());
		} else if (unit == ChronoUnit.YEARS) {
			out = out.truncatedTo(ChronoUnit.DAYS).with(firstDayOfYear());
		} else {
			out = out.plus(0, unit);
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
