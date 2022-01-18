package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;

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
	query = "SELECT DISTINCT(timeSeriesName, time) * " +
		"FROM point " +
		"WHERE timeSeriesName = ?1 " +
		"GROUP BY timeSeriesName, time " +
		"ORDER BY time, inserted DESC",
	resultClass = PointEntity.class)
@NamedNativeQuery(
	name = "Point.window",
	query = "SELECT ts AS start, " +
 		"ts + ?4::INTERVAL AS end, " +
		"SUM(p.value) AS value " +
		"FROM " +
			"generate_series(?1::TIMESTAMPTZ, ?2::TIMESTAMPTZ, ?4::INTERVAL) ts " +
		"LEFT JOIN (" +
			"SELECT DISTINCT ON (timeSeriesName, time) timeSeriesName, time, value " +
			"FROM Point " +
			"WHERE timeSeriesName = ?3 " +
			"AND time >= ?1::TIMESTAMPTZ " +
			"AND time <= ?2::TIMESTAMPTZ " +
			"GROUP BY timeSeriesName, time, inserted " +
			"ORDER BY timeSeriesName, time, inserted DESC" +
		") p " +
		"ON ts <= p.time AND ts + ?4::INTERVAL > p.time " +
		"GROUP BY ts " +
		"ORDER BY ts",
	resultSetMapping = "window")
@NamedQuery(name = "Point.minTime", query = "SELECT MIN(p.time) FROM PointEntity p WHERE p.timeSeriesName = :name")
@NamedQuery(name = "Point.maxTime", query = "SELECT MAX(p.time) FROM PointEntity p WHERE p.timeSeriesName = :name")
@SqlResultSetMapping(name = "window", classes = @ConstructorResult(
	targetClass = WindowEntity.class,
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
	@Column(nullable = false)
	private String timeSeriesName;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ", nullable = false)
	private Instant time;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ DEFAULT (now() at time zone 'utc')", nullable = false)
	@ReturnInsert
	private Instant inserted;
	@Column(nullable = false)
	private Long value;

	public String getTimeSeriesName() {
		return timeSeriesName;
	}

	public void setTimeSeriesName(String timeSeriesName) {
		this.timeSeriesName = timeSeriesName;
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
		private String timeSeriesName;
		private Instant time;
		private Instant inserted;

		public PointId() {
		}

		private PointId(PointEntity point) {
			this.timeSeriesName = point.timeSeriesName;
			this.time = point.time;
			this.inserted = point.inserted;
		}

		public String getTimeSeriesName() {
			return timeSeriesName;
		}

		public void setTimeSeriesName(String timeSeriesName) {
			this.timeSeriesName = timeSeriesName;
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
			return Objects.equals(this.timeSeriesName, other.timeSeriesName) &&
				Objects.equals(this.time, other.time) &&
				Objects.equals(this.inserted, other.inserted);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.timeSeriesName, this.time, this.inserted);
		}
	}
}