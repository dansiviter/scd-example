package uk.dansiviter.scd.rest.api;

import static java.util.Objects.requireNonNull;
import static uk.dansiviter.scd.rest.api.PersonBuilder.builder;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uk.dansiviter.scd.entity.PersonEntity;

@RecordBuilder
public record Person(
	@NotNull @Size(min = 3, max = 32) String name,
	@Min(0) int age,
	Optional<Instant> inserted)
implements PersonBuilder.With {

	public static Person from(PersonEntity entity) {
		requireNonNull(entity);
		return builder()
			.age(entity.getAge())
			.name(entity.getName())
			.inserted(Optional.of(entity.getInserted()))
			.build();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, inserted);
	}

	public static List<Person> from(List<PersonEntity> entities) {
		return entities.stream().map(Person::from).collect(Collectors.toList());
	}

	public PersonEntity toEntity() {
		return new PersonEntity(name, age);
	}
}
