package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(
	name = "Person.find",
	query = "SELECT p FROM Person p WHERE p.name = :name ORDER BY p.inserted")
@NamedQuery(
	name = "Person.all",  // potentially poor performance
	query = "SELECT p FROM Person p WHERE p.inserted = (" +
		"SELECT MAX(p0.inserted) FROM Person p0 WHERE p0.name = p.name)")
@NamedQuery(
	name = "Person.allAudit",
	query = "SELECT p FROM Person p")
@IdClass(Person.PersonId.class)
public class Person implements Serializable {
	@Id
	private String name;
	@Id
	private Instant inserted;

	private int age;

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

	public static class PersonId implements Serializable {
		private String name;
		private Instant inserted;

		public PersonId() {
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
