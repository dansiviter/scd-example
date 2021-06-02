package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "Person.find", query = "SELECT p FROM Person p WHERE p.name = :name ORDER BY p.created")
@IdClass(Person.PersonId.class)
public class Person implements Serializable {
	@Id
	private String name;
	@Id
	private Instant created;

	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instant getCreated() {
		return created;
	}

	public void setCreated(Instant created) {
		this.created = created;
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
		private Instant created;

		public PersonId() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Instant getCreated() {
			return created;
		}

		public void setCreated(Instant created) {
			this.created = created;
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
			return Objects.equals(this.name, other.name) && Objects.equals(this.created, other.created);
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.name, this.created);
		}
	}
}
