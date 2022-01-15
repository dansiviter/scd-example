package uk.dansiviter.scd;

import static uk.dansiviter.juli.annotations.Message.Level.ERROR;
import static uk.dansiviter.juli.annotations.Message.Level.WARN;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;

import org.threeten.extra.PeriodDuration;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;
import uk.dansiviter.juli.annotations.Log;
import uk.dansiviter.juli.annotations.Message;

@Log
public interface ScdLog {
	@Message(value = "{0}", level = WARN)
	void clientError(Supplier<String> object);

	@Message(value = "{0}", level = ERROR)
	void serverError(Supplier<String> object, WebApplicationException ex);

	@Message("Querying window. [timeSeriesId={0},start={1},end={2},alignment={3}].")
	void window(UUID timeSeriesId, OffsetDateTime start, OffsetDateTime end, PeriodDuration alignment);
}
