package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.ReturnInsert;

import uk.dansiviter.scd.rest.api.TimeSeries;
import uk.dansiviter.scd.rest.api.TimeSeriesBuilder;

@Entity
@IdClass(TimeSeriesEntity.TimeSeriesId.class)
@NamedNativeQuery(
	name = "TimeSeries.findByName",
	query = "SELECT * " +
		"FROM time_series ts " +
		"NATURAL JOIN (" +
			"SELECT ts0.name, MAX(ts0.inserted) AS inserted " +
			"FROM time_series ts0 " +
			"WHERE ts0.name = :name " +
			"GROUP BY ts0.name" +
	") ts",
	resultClass = TimeSeriesEntity.class)
@NamedQuery(
	name = "TimeSeries.find",
	query = "SELECT ts " +
		"FROM TimeSeriesEntity ts " +
		"WHERE ts.name = :name " +
		"ORDER BY ts.inserted DESC",
	hints = @QueryHint(name = "eclipselink.jdbc.max-rows", value = "1"))
@NamedNativeQuery(
	name = "TimeSeries.all",
	query = "SELECT DISTINCT ON (name) * " +
		"FROM time_series " +
		"GROUP BY name, inserted " +
		"ORDER BY name, inserted DESC",
	resultClass = TimeSeriesEntity.class)
@Table(name = "time_series", indexes = @Index(name = "timeseries_idx", columnList = "name, inserted DESC", unique = true))
public class TimeSeriesEntity implements BaseEntity {
	@Id
	private String name;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ DEFAULT (now() at time zone 'utc')")
	@ReturnInsert(returnOnly = true)
	private Instant inserted;
	private String description;

	public Instant getInserted() {
		return inserted;
	}

	public void setInserted(Instant inserted) {
		this.inserted = inserted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TimeSeries toRecord(List<PointEntity> points) {
		return TimeSeriesBuilder.builder()
			.name(getName())
			.inserted(getInserted())
			.description(getDescription())
			.points(PointEntity.from(points))
			.build();
	}

	public static TimeSeriesEntity from(TimeSeries timeSeries) {
		var entity = new TimeSeriesEntity();
		entity.name = timeSeries.name();
		entity.description = timeSeries.description();
		return entity;
	}


	public static class TimeSeriesId implements Serializable {
		private String name;
		private Instant inserted;

		public TimeSeriesId() { }

		public TimeSeriesId(TimeSeriesEntity timeSeries) {
			this.name = timeSeries.name;
			this.inserted = timeSeries.inserted;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Instant getInserted() {
			return inserted;
		}

		public void setInserted(Instant inserted) {
			this.inserted = inserted;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || this.getClass() != obj.getClass()) {
				return false;
			}
			var other = (TimeSeriesId) obj;
			return Objects.equals(this.name, other.name) &&
				Objects.equals(this.inserted, other.inserted);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.name, this.inserted);
		}
	}
}
