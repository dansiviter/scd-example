package uk.dansiviter.scd;

import static uk.dansiviter.jule.annotations.Message.Level.ERROR;
import static uk.dansiviter.jule.annotations.Message.Level.WARN;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import org.threeten.extra.PeriodDuration;

import uk.dansiviter.jule.annotations.Log;
import uk.dansiviter.jule.annotations.Message;

@Log
public interface ScdLog {
	@Message(value = "%s", level = WARN)
	void clientError(Supplier<String> object);

	@Message(value = "%s", level = ERROR)
	void serverError(Supplier<String> object, Throwable ex);

	@Message("Querying window. [name=%s,start=%s,end=%s,alignment=%s].")
	void window(String name, OffsetDateTime start, OffsetDateTime end, PeriodDuration alignment);
}
