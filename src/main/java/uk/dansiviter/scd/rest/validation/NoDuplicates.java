package uk.dansiviter.scd.rest.validation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The annotated element must not be {@code null}.
 * Accepts any type.
 *
 * @author Emmanuel Bernard
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NoDuplicatesValidator.class)
public @interface NoDuplicates {
	String message() default "Points contain duplicates.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
