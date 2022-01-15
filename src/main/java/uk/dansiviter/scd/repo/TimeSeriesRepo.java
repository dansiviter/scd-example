package uk.dansiviter.scd.repo;

import static java.time.ZoneOffset.UTC;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.threeten.extra.PeriodDuration;

import uk.dansiviter.scd.ScdLog;
import uk.dansiviter.scd.entity.PointEntity;
import uk.dansiviter.scd.entity.TimeSeriesEntity;
import uk.dansiviter.scd.entity.Window;

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

	public List<PointEntity> points(String name) {
		var timeSeries = em.createNamedQuery("TimeSeries.findByName", TimeSeriesEntity.class)
				.setParameter("name", name)
				.getSingleResult();
		return em.createNamedQuery("Point.allByTimeSeriesId", PointEntity.class)
			.setParameter(1, timeSeries.getId())
			.getResultList();
	}

	public List<PointEntity> points(UUID timeSeriesId) {
		return em.createNamedQuery("Point.allByTimeSeriesId", PointEntity.class)
			.setParameter(1, timeSeriesId)
			.getResultList();
	}

	public List<Window> window(UUID timeSeriesId, Optional<Temporal> start, Optional<Temporal> end, PeriodDuration alignment) {
		var startInstant = start(timeSeriesId, start, alignment);
		var endInstant = end(timeSeriesId, startInstant, end, alignment);
		this.log.window(timeSeriesId, startInstant, endInstant, alignment);
		return em.createNamedQuery("Point.window", Window.class)
			.setParameter("start", startInstant)
			.setParameter("end", endInstant)
			.setParameter("timeSeriesId", timeSeriesId)
			.setParameter("alignment", alignment.toString())
			.getResultList();
	}

	private OffsetDateTime start(UUID timeSeriesId, Optional<Temporal> start, PeriodDuration alignment) {
		if (start.isPresent()) {
			return start.map(TimeSeriesRepo::toDateTime).orElseThrow();
		}

		var result = em.createNamedQuery("Point.minTime", Instant.class)
			.setParameter("timeSeriesId", timeSeriesId)
			.getSingleResult()
			.atOffset(UTC);

		result = truncateTo(result, lastWholeUnit(alignment));
		return result;
	}

	private OffsetDateTime end(UUID timeSeriesId, OffsetDateTime start, Optional<Temporal> end, PeriodDuration alignment) {
		if (end.isPresent()) {
			return end.map(TimeSeriesRepo::toDateTime).orElseThrow();
		}

		var result = em.createNamedQuery("Point.maxTime", Instant.class)
			.setParameter("timeSeriesId", timeSeriesId)
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
