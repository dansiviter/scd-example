package uk.dansiviter.scd;

import static uk.dansiviter.juli.annotations.Message.Level.ERROR;
import static uk.dansiviter.juli.annotations.Message.Level.WARN;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import org.threeten.extra.PeriodDuration;

import uk.dansiviter.juli.annotations.Log;
import uk.dansiviter.juli.annotations.Message;

@Log
public interface ScdLog {
	@Message(value = "{0}", level = WARN)
	void clientError(Supplier<String> object);

	@Message(value = "{0}", level = ERROR)
	void serverError(Supplier<String> object, Throwable ex);

	@Message("Querying window. [name={0},start={1},end={2},alignment={3}].")
	void window(String name, OffsetDateTime start, OffsetDateTime end, PeriodDuration alignment);
}
