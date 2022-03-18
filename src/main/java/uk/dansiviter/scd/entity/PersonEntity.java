package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import org.eclipse.persistence.annotations.ReturnInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@NamedNativeQuery(
	name = "Person.find",
	query = "SELECT DISTINCT ON (name) * " +
		"FROM person " +
		"WHERE name = ?1 " +
		"GROUP BY name, inserted " +
		"ORDER BY name, inserted DESC",
	resultClass = PersonEntity.class)
@NamedNativeQuery(
	name = "Person.find.instant",
	query = "SELECT DISTINCT ON (name) * " +
		"FROM person " +
		"WHERE name = ?1 " +
		"AND inserted <= ?2 " +
		"GROUP BY name, inserted " +
		"ORDER BY name, inserted DESC",
	resultClass = PersonEntity.class)
@NamedNativeQuery(
	name = "Person.find.audit",
	query = "SELECT * " +
		"FROM person " +
		"WHERE name = ?1 " +
		"ORDER BY inserted DESC",
	resultClass = PersonEntity.class)
@NamedNativeQuery(
	name = "Person.all",
	query = "SELECT DISTINCT ON (name) * " +
		"FROM person " +
		"GROUP BY name, inserted " +
		"ORDER BY name, inserted DESC",
	resultClass = PersonEntity.class)
@NamedNativeQuery(
	name = "Person.all.audit",
	query = "SELECT * " +
		"FROM person " +
		"ORDER BY inserted DESC",
	resultClass = PersonEntity.class)
@NamedNativeQuery(
	name = "Person.all.instant",
	query = "SELECT DISTINCT ON (name) * " +
		"FROM person " +
		"WHERE inserted <= ?2 " +
		"GROUP BY name, inserted " +
		"ORDER BY name, inserted DESC",
	resultClass = PersonEntity.class)
@IdClass(PersonEntity.PersonId.class)
@Table(name = "person", indexes = @Index(name = "person_idx", columnList = "name, inserted DESC"))
public class PersonEntity implements BaseEntity {
	@Id
	@Column(nullable = false)
	private String name;
	@Id
	@Column(columnDefinition = "TIMESTAMPTZ", nullable = false)
	@ReturnInsert
	private Instant inserted;

	private int age;

	public PersonEntity() { }

	public PersonEntity(String name, int age, Instant inserted) {
		this.name = name;
		this.age = age;
		this.inserted = inserted;
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
		var other = (PersonEntity) obj;
		return Objects.equals(this.name, other.name) &&
			this.age == other.age;
	}

	@PrePersist
	public void prePersist() {
		setInserted(Instant.now());
	}

	public PersonId toId() {
		return new PersonId(this);
	}

	public static class PersonId implements Serializable {
		private String name;
		private Instant inserted;

		public PersonId() {
		}

		private PersonId(PersonEntity person) {
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
