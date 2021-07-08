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
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.ReturnInsert;

@Entity
@NamedQuery(
	name = "Person.find",
	query = "SELECT p " +
		"FROM Person p " +
		"WHERE p.name = :name " +
		"ORDER BY p.inserted DESC",
	hints = @QueryHint(name = "eclipselink.jdbc.max-rows", value = "1"))
@NamedQuery(
	name = "Person.find.instant",
	query = "SELECT p " +
		"FROM Person p " +
		"WHERE p.name = :name " +
		"AND p.inserted <= :instant " +
		"ORDER BY p.inserted DESC",
	hints = @QueryHint(name = "eclipselink.jdbc.max-rows", value = "1"))
@NamedQuery(
	name = "Person.audit",
	query = "SELECT p " +
		"FROM Person p " +
		"WHERE p.name = :name " +
		"ORDER BY p.inserted DESC")
	@NamedQuery(
	name = "Person.all",
	query = "SELECT p " +
		"FROM Person p " +
		"WHERE p.inserted = (" +
			"SELECT MAX(p0.inserted) " +
			"FROM Person p0 " +
			"WHERE p0.name = p.name" +
		")")
@NamedNativeQuery(
	name = "Person.all.native",
	query = "SELECT * " +
		"FROM Person p " +
		"NATURAL JOIN (" +
			"SELECT p0.name, MAX(p0.inserted) AS inserted " +
			"FROM Person p0 " +
			"GROUP BY p0.name" +
	") p0",
	resultClass = Person.class)
@NamedQuery(
	name = "Person.all.instant",
	query = "SELECT p " +
	  "FROM Person p " +
		"WHERE p.inserted = (" +
			"SELECT MAX(p0.inserted) " +
			"FROM Person p0 " +
			"WHERE p0.name = p.name " +
			"AND p0.inserted <= :instant " +
		")")
@NamedQuery(
	name = "Person.allAudit",
	query = "SELECT p FROM Person p")
@IdClass(Person.PersonId.class)
@Table(indexes = @Index(columnList = "uuid", unique = true))
public class Person implements Serializable {
	@Column(columnDefinition = "UUID DEFAULT gen_random_uuid() NOT NULL", unique = true, insertable = false, updatable = false)
	@ReturnInsert
	private UUID uuid;
	@Id
	@Column(nullable = false)
	private String name;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ DEFAULT (now() at time zone 'utc')", nullable = false, insertable = false)
	@ReturnInsert
	private Instant inserted;

	private int age;

	public Person() { }

	public Person(String name, int age, Instant inserted) {
		this.name = name;
		this.age = age;
		this.inserted = inserted;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		var other = (Person) obj;
		return Objects.equals(this.name, other.name) &&
			this.age == other.age;
	}


	public PersonId toId() {
		return new PersonId(this);
	}

	public static class PersonId implements Serializable {
		private String name;
		private Instant inserted;

		public PersonId() {
		}

		private PersonId(Person person) {
			this.name = person.name;
			this.inserted = person.inserted;
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
			var other = (PersonId) obj;
			return Objects.equals(this.name, other.name) && Objects.equals(this.inserted, other.inserted);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.name, this.inserted);
		}
	}
}
