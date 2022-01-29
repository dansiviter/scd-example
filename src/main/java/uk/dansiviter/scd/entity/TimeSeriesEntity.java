package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.ReturnInsert;

@Entity
@IdClass(TimeSeriesEntity.TimeSeriesId.class)
@NamedNativeQuery(
	name = "TimeSeries.findByName",
	query = "SELECT * " +
		"FROM time_series ts " +
		"NATURAL JOIN (" +
			"SELECT ts0.id, MAX(ts0.inserted) AS inserted " +
			"FROM time_series ts0 " +
			"WHERE ts0.name = :name " +
			"GROUP BY ts0.id" +
	") ts",
	resultClass = TimeSeriesEntity.class)
@NamedQuery(
	name = "TimeSeries.find",
	query = "SELECT ts " +
		"FROM TimeSeriesEntity ts " +
		"WHERE ts.id = :id " +
		"ORDER BY ts.inserted DESC",
	hints = @QueryHint(name = "eclipselink.jdbc.max-rows", value = "1"))
@NamedNativeQuery(
	name = "TimeSeries.all",
	query = "SELECT * " +
		"FROM timeseries " +
		"NATURAL JOIN (" +
			"SELECT ts0.id, MAX(ts0.inserted) AS inserted " +
			"FROM time_series ts0 " +
			"GROUP BY ts0.id" +
	") ts",
	resultClass = TimeSeriesEntity.class)
@Table(name = "time_series", indexes = @Index(name = "timeseries_idx", columnList = "id, name, inserted DESC", unique = true))
public class TimeSeriesEntity implements BaseEntity {
	@Id
	@Column(columnDefinition = "UUID", unique = true, nullable = false)
	private UUID id;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ DEFAULT (now() at time zone 'utc')", nullable = false)
	@ReturnInsert
	private Instant inserted;
	@Column(nullable = false)
	private String name;
	private String description;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	@PrePersist
	public void prePersist() {
		if (getId() == null) {
			setId(UUID_GENERATOR.get());
		}
	}

	public static class TimeSeriesId implements Serializable {
		private UUID id;
		private Instant inserted;

		public TimeSeriesId() { }

		public TimeSeriesId(TimeSeriesEntity timeSeries) {
			this.id = timeSeries.id;
			this.inserted = timeSeries.inserted;
		}

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
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
			return Objects.equals(this.id, other.id) && Objects.equals(this.inserted, other.inserted);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.id, this.inserted);
		}
	}
}
