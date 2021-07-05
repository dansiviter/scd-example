package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

@Entity
@NamedNativeQuery(
	name = "Point.allByName",
	query = "SELECT * " +
	"FROM Point p " +
	"NATURAL JOIN (" +
		"SELECT name, time, MAX(inserted) AS inserted " +
		"FROM point " +
		"WHERE name = ?1 " +
		"GROUP BY name, time" +
	") p0",
	resultClass = Point.class)
@NamedNativeQuery(
	name = "Point.window",
	query = "SELECT ts::TIMESTAMP AT TIME ZONE 'UTC' AS start, ts::TIMESTAMP AT TIME ZONE 'UTC' + 'PT23H59M59.999999S' AS end, SUM(p.value) AS value " +
		"FROM Point p " +
		"NATURAL JOIN (" +
			"SELECT name, time, MAX(inserted) AS inserted FROM point " +
			"WHERE name = ?3 " +
			"GROUP BY name, time" +
		") AS p0 " +
		"RIGHT JOIN generate_series(?1::DATE, ?2::DATE, '1 day'::INTERVAL) ts ON ts = p.time::DATE " +
		"GROUP BY ts " +
		"ORDER BY ts",
	resultSetMapping = "window")
@SqlResultSetMapping(name = "window", classes = @ConstructorResult(
	targetClass = Window.class,
	columns = {
		@ColumnResult(name = "start", type = Timestamp.class),
		@ColumnResult(name = "end", type = Timestamp.class),
		@ColumnResult(name = "value", type = Long.class)
	})
)
@IdClass(Point.PointId.class)
public class Point implements Serializable {
	@Id
	private String name;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ(6)", nullable = false)
	private Instant time;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ(6)", nullable = false)
	private Instant inserted;

	@Column(nullable = false)
	private long value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}


	public PointId toId() {
		return new PointId(this);
	}

	public static class PointId implements Serializable {
		private String name;
		private Instant time;
		private Instant inserted;

		public PointId() {
		}

		private PointId(Point point) {
			this.name = point.name;
			this.time = point.time;
			this.inserted = point.inserted;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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
			return Objects.equals(this.name, other.name) &&
				Objects.equals(this.time, other.time) &&
				Objects.equals(this.inserted, other.inserted);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.name, this.time, this.inserted);
		}
	}
}
