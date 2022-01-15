package uk.dansiviter.scd.entity;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Supplier;

import javax.persistence.PreUpdate;

import uk.dansiviter.uuid.UuidFactories;

public interface BaseEntity extends Serializable {
	static final Supplier<UUID> UUID_GENERATOR = UuidFactories.type6();

	@PreUpdate
	public default void update() {
		throw new IllegalStateException();
	}
}
