package uk.dansiviter.scd.rest.api;

import static java.util.Objects.requireNonNull;
import static uk.dansiviter.scd.rest.api.PersonBuilder.builder;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.soabase.recordbuilder.core.RecordBuilder;
import uk.dansiviter.scd.entity.PersonEntity;

@RecordBuilder
public record Person(
	UUID uuid,
	@NotNull @Size(min = 3, max = 32) String name,
	Instant inserted,
	@Min(0) int age)
implements PersonBuilder.With, Cachable {

	public static Person from(PersonEntity entity) {
		requireNonNull(entity);
		return builder()
			.age(entity.getAge())
			.name(entity.getName())
			.inserted(entity.getInserted())
			.uuid(entity.getUuid())
			.build();
	}

	@Override
	public long hash() {
		return Objects.hash(name, inserted);
	}

	public static List<Person> from(List<PersonEntity> entities) {
		return entities.stream().map(Person::from).collect(Collectors.toList());
	}

	public PersonEntity toEntity() {
		return new PersonEntity(name, age, inserted);
	}
}
