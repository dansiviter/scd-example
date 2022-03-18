package uk.dansiviter.scd.rest.validation;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uk.dansiviter.scd.rest.api.Point;

/**
 * Validator instance for {@link NoDuplicates}.
 */
public class NoDuplicatesValidator implements ConstraintValidator<NoDuplicates, List<Point>> {
	@Override
	public boolean isValid(List<Point> value, ConstraintValidatorContext context) {
		if (isNull(value)) {
			return true;
		}
		var duplicates = value.stream()
			.collect(Collectors.groupingBy(Point::time, Collectors.counting()))
			.entrySet().stream()
			.filter(e -> e.getValue() > 1)
			.map(Entry::getKey)
			.collect(Collectors.toSet());
		if (duplicates.isEmpty()) {
			return true;
		}
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("Duplicate points! ".concat(duplicates.toString()))
			.addConstraintViolation();
		return false;
	}
}
