package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.ReturnInsert;

@Entity
@NamedNativeQuery(
	name = "Point.allByTimeSeriesId",
	query = "SELECT * " +
	"FROM Point p " +
	"NATURAL JOIN (" +
		"SELECT timeSeriesId, time, MAX(inserted) AS inserted " +
		"FROM point " +
		"WHERE timeSeriesId = ?1 " +
		"GROUP BY timeSeriesId, time" +
	") p0",
	resultClass = PointEntity.class)
@NamedNativeQuery(
	name = "Point.window",
	query = "SELECT " +
			"ts AS start, " +
			"ts + CAST(:alignment AS INTERVAL) AS end, " +
			"SUM(p.value) AS value " +
		"FROM Point p " +
		"NATURAL JOIN (" +
			"SELECT timeSeriesId, time, MAX(inserted) AS inserted FROM point " +
			"WHERE timeSeriesId = :timeSeriesId " +
			"AND time >= :start " +
			"AND time <= :end " +
			"GROUP BY timeSeriesId, time" +
		") AS p0 " +
		"RIGHT JOIN generate_series(:start, :end, CAST(:alignment AS INTERVAL)) ts " +
		  "ON ts <= p.time AND ts + CAST(:alignment AS INTERVAL) > p.time " +
		"GROUP BY ts " +
		"ORDER BY ts",
	resultSetMapping = "window")
@NamedQuery(name = "Point.minTime", query = "SELECT MIN(p.time) FROM PointEntity p WHERE p.timeSeriesId = :timeSeriesId")
@NamedQuery(name = "Point.maxTime", query = "SELECT MAX(p.time) FROM PointEntity p WHERE p.timeSeriesId = :timeSeriesId")
@SqlResultSetMapping(name = "window", classes = @ConstructorResult(
	targetClass = Window.class,
	columns = {
		@ColumnResult(name = "start", type = OffsetDateTime.class),
		@ColumnResult(name = "end", type = OffsetDateTime.class),
		@ColumnResult(name = "value", type = Long.class)
	})
)
@Table(name = "point")
@IdClass(PointEntity.PointId.class)
public class PointEntity implements BaseEntity {
	@Id
	@Column(columnDefinition = "UUID", nullable = false)
	private UUID timeSeriesId;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ", nullable = false)
	private Instant time;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ DEFAULT (now() at time zone 'utc')", nullable = false)
	@ReturnInsert
	private Instant inserted;
	@Column(nullable = false)
	private Long value;

	public UUID getTimeSeriesId() {
		return timeSeriesId;
	}

	public void setTimeSeriesId(UUID timeSeriesId) {
		this.timeSeriesId = timeSeriesId;
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public Instant getInserted() {
		return inserted;
	}

	public void setInserted(Instant inserted) {
		this.inserted = inserted;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}


	public PointId toId() {
		return new PointId(this);
	}

	public static class PointId implements Serializable {
		private UUID timeSeriesId;
		private Instant time;
		private Instant inserted;

		public PointId() {
		}

		private PointId(PointEntity point) {
			this.timeSeriesId = point.timeSeriesId;
			this.time = point.time;
			this.inserted = point.inserted;
		}

		public UUID getTimeSeriesId() {
			return timeSeriesId;
		}

		public void setTimeSeriesId(UUID timeSeriesId) {
			this.timeSeriesId = timeSeriesId;
		}

		public Instant getTime() {
			return time;
		}

		public void setTime(Instant time) {
			this.time = time;
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
			var other = (PointId) obj;
			return Objects.equals(this.timeSeriesId, other.timeSeriesId) &&
				Objects.equals(this.time, other.time) &&
				Objects.equals(this.inserted, other.inserted);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.timeSeriesId, this.time, this.inserted);
		}
	}
}
