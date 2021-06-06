package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(
	name = "Person.find",
	query = "SELECT p " +
		"FROM Person p " +
		"WHERE p.name = :name " +
		"ORDER BY p.inserted DESC")
@NamedQuery(
	name = "Person.find.instant",
	query = "SELECT p " +
		"FROM Person p " +
		"WHERE p.name = :name " +
		"AND p.inserted <= :instant " +
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
		"FROM PERSON p " +
		"WHERE (p.name, p.inserted) IN (" +
			"SELECT p0.name, MAX(p0.inserted) " +
			"FROM Person p0 " +
			"GROUP BY p0.name" +
	")",
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
public class Person implements Serializable {
	@Id
	private String name;
	@Id
	@Column(columnDefinition = "TIMESTAMP(9) WITH TIME ZONE")
	private Instant inserted;

	private int age;

	public Person() { }

	public Person(String name, int age, Instant inserted) {
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
